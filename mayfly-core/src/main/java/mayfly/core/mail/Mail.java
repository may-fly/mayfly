package mayfly.core.mail;

import mayfly.core.util.ArrayUtils;
import mayfly.core.util.Assert;
import mayfly.core.util.FileUtils;
import mayfly.core.util.IOUtils;
import mayfly.core.util.PropertiesUtils;
import mayfly.core.util.StringUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.FileTypeMap;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author meilin.huang
 * @date 2020-07-23 3:10 下午
 */
public class Mail {
    /**
     * 邮箱帐户信息以及一些客户端配置信息
     */
    private final MailAccount mailAccount;

    /**
     * 收件人列表
     */
    private String[] tos;
    /**
     * 抄送人列表（carbon copy）
     */
    private String[] ccs;
    /**
     * 密送人列表（blind carbon copy）
     */
    private String[] bccs;
    /**
     * 回复地址(reply-to)
     */
    private String[] reply;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 是否为HTML
     */
    private boolean isHtml;
    /**
     * 正文、附件和图片的混合部分
     */
    private final Multipart multipart = new MimeMultipart();
    /**
     * 是否使用全局会话，默认为false
     */
    private boolean useGlobalSession = false;

    private static final Map<String, MailAccount> MAIL_ACCOUNT_CACHE = new ConcurrentHashMap<>(4);

    /**
     * 创建邮件客户端
     *
     * @param mailAccount 邮件帐号
     * @return {@link Mail}
     */
    public static Mail create(MailAccount mailAccount) {
        Assert.notNull(mailAccount, "mailAccount不能为空");
        return new Mail(mailAccount);
    }

    /**
     * 根据properties文件创建Mail对象
     *
     * @param properties properties文件名
     * @param prefix     属性前缀
     * @return mail对象
     */
    public static Mail createBy(String properties, String prefix) {
        Assert.notEmpty(properties, "mail properties路径不能为空");
        String key = properties + "_" + Optional.ofNullable(prefix).orElse("");
        MailAccount mailAccount = MAIL_ACCOUNT_CACHE.computeIfAbsent(key, k -> {
            try {
                return MailAccount.builder().fromProperties(PropertiesUtils.load(properties, null), prefix).build();
            } catch (IOException e) {
                throw new RuntimeException("读取mail properties文件错误", e);
            }
        });
        return create(mailAccount);
    }

    // --------------------------------------------------------------- Constructor start

    /**
     * 构造
     *
     * @param mailAccount 邮件帐户，如果为null使用默认配置文件的全局邮件配置
     */
    public Mail(MailAccount mailAccount) {
        this.mailAccount = mailAccount;
    }
    // --------------------------------------------------------------- Constructor end

    // --------------------------------------------------------------- setter

    /**
     * 设置多个收件人
     *
     * @param tos 收件人列表
     * @return this
     */
    public Mail tos(String... tos) {
        this.tos = tos;
        return this;
    }

    /**
     * 设置多个抄送人（carbon copy）
     *
     * @param ccs 抄送人列表
     * @return this
     */
    public Mail ccs(String... ccs) {
        this.ccs = ccs;
        return this;
    }

    /**
     * 设置多个密送人（blind carbon copy）
     *
     * @param bccs 密送人列表
     * @return this
     */
    public Mail bccs(String... bccs) {
        this.bccs = bccs;
        return this;
    }

    /**
     * 设置多个回复地址(reply-to)
     *
     * @param reply 回复地址(reply-to)列表
     * @return this
     */
    public Mail reply(String... reply) {
        this.reply = reply;
        return this;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     * @return this
     */
    public Mail title(String title) {
        this.title = title;
        return this;
    }

    /**
     * 设置正文<br>
     * 正文可以是普通文本也可以是HTML（默认普通文本），可以通过调用{@link #html(boolean)} 设置是否为HTML
     *
     * @param content 正文
     * @return this
     */
    public Mail content(String content) {
        this.content = content;
        return this;
    }

    /**
     * 设置是否是HTML
     *
     * @param isHtml 是否为HTML
     * @return this
     */
    public Mail html(boolean isHtml) {
        this.isHtml = isHtml;
        return this;
    }

    /**
     * 设置正文
     *
     * @param content 正文内容
     * @param isHtml  是否为HTML
     * @return this
     */
    public Mail content(String content, boolean isHtml) {
        return content(content).html(isHtml);
    }

    /**
     * 设置文件类型附件，文件可以是图片文件，此时自动设置cid（正文中引用图片），默认cid为文件名
     *
     * @param files 附件文件列表
     * @return this
     */
    public Mail files(File... files) {
        if (ArrayUtils.isEmpty(files)) {
            return this;
        }

        final DataSource[] attachments = new DataSource[files.length];
        for (int i = 0; i < files.length; i++) {
            attachments[i] = new FileDataSource(files[i]);
        }
        return attachments(attachments);
    }

    /**
     * 增加附件或图片，附件使用{@link DataSource} 形式表示，可以使用{@link FileDataSource}包装文件表示文件附件
     *
     * @param attachments 附件列表
     * @return this
     */
    public Mail attachments(DataSource... attachments) {
        if (!ArrayUtils.isEmpty(attachments)) {
            final Charset charset = this.mailAccount.getCharset();
            MimeBodyPart bodyPart;
            String nameEncoded;
            try {
                for (DataSource attachment : attachments) {
                    bodyPart = new MimeBodyPart();
                    bodyPart.setDataHandler(new DataHandler(attachment));
                    nameEncoded = encodeText(attachment.getName(), charset);
                    // 普通附件文件名
                    bodyPart.setFileName(nameEncoded);
                    if (attachment.getContentType().startsWith("image/")) {
                        // 图片附件，用于正文中引用图片
                        bodyPart.setContentID(nameEncoded);
                    }
                    this.multipart.addBodyPart(bodyPart);
                }
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
        return this;
    }

    /**
     * 增加图片，图片的键对应到邮件模板中的占位字符串，图片类型默认为"image/jpeg"
     *
     * @param cid         图片与占位符，占位符格式为cid:${cid}
     * @param imageStream 图片文件
     * @return this
     */
    public Mail addImage(String cid, InputStream imageStream) {
        return addImage(cid, imageStream, null);
    }

    /**
     * 增加图片，图片的键对应到邮件模板中的占位字符串
     *
     * @param cid         图片与占位符，占位符格式为cid:${cid}
     * @param imageStream 图片流，不关闭
     * @param contentType 图片类型，null赋值默认的"image/jpeg"
     * @return this
     * @since 4.6.3
     */
    public Mail addImage(String cid, InputStream imageStream, String contentType) {
        ByteArrayDataSource imgSource;
        try {
            imgSource = new ByteArrayDataSource(imageStream, Objects.requireNonNullElse(contentType, "image/jpeg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imgSource.setName(cid);
        return attachments(imgSource);
    }

    /**
     * 增加图片，图片的键对应到邮件模板中的占位字符串
     *
     * @param cid       图片与占位符，占位符格式为cid:${cid}
     * @param imageFile 图片文件
     * @return this
     */
    public Mail addImage(String cid, File imageFile) {
        InputStream in = null;
        try {
            in = FileUtils.getInputStream(imageFile);
            return addImage(cid, in, FileTypeMap.getDefaultFileTypeMap().getContentType(imageFile));
        } finally {
            IOUtils.close(in);
        }
    }

    /**
     * 设置字符集编码
     *
     * @param charset 字符集编码
     * @return this
     * @see MailAccount#setCharset(Charset)
     */
    public Mail charset(Charset charset) {
        this.mailAccount.setCharset(charset);
        return this;
    }

    /**
     * 设置是否使用全局会话，默认为true
     *
     * @param isUseGlobalSession 是否使用全局会话，默认为true
     * @return this
     */
    public Mail useGlobalSession(boolean isUseGlobalSession) {
        this.useGlobalSession = isUseGlobalSession;
        return this;
    }
    // --------------------------------------------------------------- Getters and Setters end

    /**
     * 发送
     *
     * @return message-id
     */
    public String send() throws MessagingException {
        return doSend();
    }

    // --------------------------------------------------------------- Private method start

    /**
     * 执行发送
     *
     * @return message-id
     * @throws MessagingException 发送异常
     */
    private String doSend() throws MessagingException {
        final MimeMessage mimeMessage = buildMsg();
        Transport.send(mimeMessage);
        return mimeMessage.getMessageID();
    }

    /**
     * 构建消息
     *
     * @return {@link MimeMessage}消息
     * @throws MessagingException 消息异常
     */
    private MimeMessage buildMsg() throws MessagingException {
        final Charset charset = this.mailAccount.getCharset();
        final MimeMessage msg = new MimeMessage(getSession(this.useGlobalSession));
        // 发件人
        final String from = this.mailAccount.getFrom();
        if (StringUtils.isEmpty(from)) {
            // 用户未提供发送方，则从Session中自动获取
            msg.setFrom();
        } else {
            msg.setFrom(parseFirstAddress(from, charset));
        }
        // 标题
        msg.setSubject(this.title, charset.name());
        // 发送时间
        msg.setSentDate(new Date());
        // 内容和附件
        msg.setContent(buildContent(charset));
        // 收件人
        msg.setRecipients(MimeMessage.RecipientType.TO, parseAddressFromStrs(this.tos, charset));
        // 抄送人
        if (!ArrayUtils.isEmpty(this.ccs)) {
            msg.setRecipients(MimeMessage.RecipientType.CC, parseAddressFromStrs(this.ccs, charset));
        }
        // 密送人
        if (!ArrayUtils.isEmpty(this.bccs)) {
            msg.setRecipients(MimeMessage.RecipientType.BCC, parseAddressFromStrs(this.bccs, charset));
        }
        // 回复地址(reply-to)
        if (!ArrayUtils.isEmpty(this.reply)) {
            msg.setReplyTo(parseAddressFromStrs(this.reply, charset));
        }

        return msg;
    }

    /**
     * 构建邮件信息主体
     *
     * @param charset 编码
     * @return 邮件信息主体
     * @throws MessagingException 消息异常
     */
    private Multipart buildContent(Charset charset) throws MessagingException {
        // 正文
        final MimeBodyPart body = new MimeBodyPart();
        body.setContent(content, String.format("text/%s; charset=%s", isHtml ? "html" : "plain", charset));
        this.multipart.addBodyPart(body);

        return this.multipart;
    }

    /**
     * 获取默认邮件会话<br>
     * 如果为全局单例的会话，则全局只允许一个邮件帐号，否则每次发送邮件会新建一个新的会话
     *
     * @param isSingleton 是否使用单例Session
     * @return 邮件会话 {@link Session}
     */
    private Session getSession(boolean isSingleton) {
        return isSingleton ? Session.getDefaultInstance(mailAccount.getSmtpProps(), mailAccount.getUserPassAuthenticator())
                : Session.getInstance(mailAccount.getSmtpProps(), mailAccount.getUserPassAuthenticator());
    }

    /**
     * 将多个字符串邮件地址转为{@link InternetAddress}列表<br>
     * 单个字符串地址可以是多个地址合并的字符串
     *
     * @param addrStrs 地址数组
     * @param charset  编码（主要用于中文用户名的编码）
     * @return 地址数组
     */
    public static InternetAddress[] parseAddressFromStrs(String[] addrStrs, Charset charset) {
        final List<InternetAddress> resultList = new ArrayList<>(addrStrs.length);
        InternetAddress[] addrs;
        for (String addrStr : addrStrs) {
            addrs = parseAddress(addrStr, charset);
            if (!ArrayUtils.isEmpty(addrs)) {
                Collections.addAll(resultList, addrs);
            }
        }
        return resultList.toArray(new InternetAddress[0]);
    }

    /**
     * 解析第一个地址
     *
     * @param address 地址字符串
     * @param charset 编码
     * @return 地址列表
     */
    public static InternetAddress parseFirstAddress(String address, Charset charset) {
        final InternetAddress[] internetAddresses = parseAddress(address, charset);
        if (ArrayUtils.isEmpty(internetAddresses)) {
            try {
                return new InternetAddress(address);
            } catch (AddressException e) {
                throw new RuntimeException(e);
            }
        }
        return internetAddresses[0];
    }

    /**
     * 将一个地址字符串解析为多个地址<br>
     * 地址间使用" "、","、";"分隔
     *
     * @param address 地址字符串
     * @param charset 编码
     * @return 地址列表
     */
    public static InternetAddress[] parseAddress(String address, Charset charset) {
        InternetAddress[] addresses;
        try {
            addresses = InternetAddress.parse(address);
        } catch (AddressException e) {
            throw new RuntimeException(e);
        }
        // 编码用户名
        if (!ArrayUtils.isEmpty(addresses)) {
            for (InternetAddress internetAddress : addresses) {
                try {
                    internetAddress.setPersonal(internetAddress.getPersonal(), charset.name());
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return addresses;
    }

    /**
     * 编码中文字符<br>
     * 编码失败返回原字符串
     *
     * @param text    被编码的文本
     * @param charset 编码
     * @return 编码后的结果
     */
    public static String encodeText(String text, Charset charset) {
        try {
            return MimeUtility.encodeText(text, charset.name(), null);
        } catch (UnsupportedEncodingException e) {
            // ignore
        }
        return text;
    }


}
