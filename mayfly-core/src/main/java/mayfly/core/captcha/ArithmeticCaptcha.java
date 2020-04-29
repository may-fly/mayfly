package mayfly.core.captcha;

import mayfly.core.captcha.base.BaseCaptcha;
import mayfly.core.util.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Stack;

/**
 * 算术验证码
 *
 * @author meilin.huang
 * @date 2020-04-29 11:22 上午
 */
public class ArithmeticCaptcha extends BaseCaptcha {

    /**
     * 计算公式
     */
    private String arithmeticString;

    protected ArithmeticCaptcha() {}

    @Override
    public boolean out(OutputStream out) {
        checkChars();
        return graphicsImage(arithmeticString.toCharArray(), out);
    }

    @Override
    public String base64Encode() {
        return "data:image/png;base64,";
    }

    /**
     * 重写生成随机验证码方法
     *
     * @return 验证码字符数组
     */
    @Override
    protected char[] generateChars() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(num(10));
            if (i < len - 1) {
                int type = num(1, 4);
                if (type == 1) {
                    sb.append("+");
                } else if (type == 2) {
                    sb.append("-");
                } else if (type == 3) {
                    sb.append("x");
                }
            }
        }
        // 计算表达式结果
        String result = String.valueOf(Calculator.conversion(sb.toString().replaceAll("x", "*")));
        sb.append("=?");
        arithmeticString = sb.toString();
        return result.toCharArray();
    }

    /**
     * 生成验证码图形
     *
     * @param strs 验证码
     * @param out  输出流
     * @return boolean
     */
    private boolean graphicsImage(char[] strs, OutputStream out) {
        try {
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = (Graphics2D) bi.getGraphics();
            // 填充背景
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, width, height);
            // 抗锯齿
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // 画干扰圆
            drawOval(2, g2d);
            // 画字符串
            g2d.setFont(getFont());
            FontMetrics fontMetrics = g2d.getFontMetrics();
            int fW = width / strs.length;
            // 每一个字符所占的宽度
            int fSp = (fW - (int) fontMetrics.getStringBounds("8", g2d).getWidth()) / 2;
            // 字符的左右边距
            for (int i = 0; i < strs.length; i++) {
                g2d.setColor(color());
                int fY = height - ((height - (int) fontMetrics.getStringBounds(String.valueOf(strs[i]), g2d).getHeight()) >> 1);
                // 文字的纵坐标
                g2d.drawString(String.valueOf(strs[i]), i * fW + fSp + 3, fY - 3);
            }
            g2d.dispose();
            ImageIO.write(bi, "png", out);
            out.flush();
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            IOUtils.close(out);
        }
    }

    public String getArithmeticString() {
        checkChars();
        return arithmeticString;
    }

    public void setArithmeticString(String arithmeticString) {
        this.arithmeticString = arithmeticString;
    }


    /**
     * 简单加减乘算术
     */
    static class Calculator {
        /**
         * 后缀式栈
         */
        private final Stack<String> postfixStack = new Stack<>();
        /**
         * 运算符栈
         */
        private final Stack<Character> opStack = new Stack<>();
        /**
         * 运用运算符ASCII码-40做索引的运算符优先级
         */
        private final int[] operatPriority = new int[]{0, 3, 2, 1, -1, 1, 0, 2};

        public static String conversion(String expression) {
            double result = 0;
            Calculator cal = new Calculator();
            try {
                expression = transform(expression);
                return cal.calculate(expression);
            } catch (Exception e) {
                // skip
                return null;
            }
        }

        /**
         * 将表达式中负数的符号更改
         *
         * @param expression 例如-2+-1*(-3E-2)-(-1) 被转为 ~2+~1*(~3E~2)-(~1)
         * @return
         */
        private static String transform(String expression) {
            char[] arr = expression.toCharArray();
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == '-') {
                    if (i == 0) {
                        arr[i] = '~';
                    } else {
                        char c = arr[i - 1];
                        if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == 'E' || c == 'e') {
                            arr[i] = '~';
                        }
                    }
                }
            }
            if (arr[0] == '~' || arr[1] == '(') {
                arr[0] = '-';
                return "0" + new String(arr);
            } else {
                return new String(arr);
            }
        }

        /**
         * 按照给定的表达式计算
         *
         * @param expression 要计算的表达式例如:5+12*3
         * @return
         */
        public String calculate(String expression) {
            Stack<String> resultStack = new Stack<>();
            prepare(expression);
            Collections.reverse(postfixStack);// 将后缀式栈反转
            String firstValue, secondValue, currentValue;// 参与计算的第一个值，第二个值和算术运算符
            while (!postfixStack.isEmpty()) {
                currentValue = postfixStack.pop();
                if (!isOperator(currentValue.charAt(0))) {// 如果不是运算符则存入操作数栈中
                    currentValue = currentValue.replace("~", "-");
                    resultStack.push(currentValue);
                } else {// 如果是运算符则从操作数栈中取两个值和该数值一起参与运算
                    secondValue = resultStack.pop();
                    firstValue = resultStack.pop();

                    // 将负数标记符改为负号
                    firstValue = firstValue.replace("~", "-");
                    secondValue = secondValue.replace("~", "-");

                    String tempResult = calculate(Integer.parseInt(firstValue), Integer.parseInt(secondValue), currentValue.charAt(0));
                    resultStack.push(tempResult);
                }
            }
            return resultStack.pop();
        }

        /**
         * 数据准备阶段将表达式转换成为后缀式栈
         *
         * @param expression
         */
        private void prepare(String expression) {
            // 运算符放入栈底元素逗号，此符号优先级最低
            opStack.push(',');
            char[] arr = expression.toCharArray();
            // 当前字符的位置
            int currentIndex = 0;
            // 上次算术运算符到本次算术运算符的字符的长度便于或者之间的数值
            int count = 0;
            // 当前操作符和栈顶操作符
            char currentOp, peekOp;
            for (int i = 0; i < arr.length; i++) {
                currentOp = arr[i];
                // 如果当前字符是运算符
                if (isOperator(currentOp)) {
                    if (count > 0) {
                        // 取两个运算符之间的数字
                        postfixStack.push(new String(arr, currentIndex, count));
                    }
                    peekOp = opStack.peek();
                    // 遇到反括号则将运算符栈中的元素移除到后缀式栈中直到遇到左括号
                    if (currentOp == ')') {
                        while (opStack.peek() != '(') {
                            postfixStack.push(String.valueOf(opStack.pop()));
                        }
                        opStack.pop();
                    } else {
                        while (currentOp != '(' && peekOp != ',' && compare(currentOp, peekOp)) {
                            postfixStack.push(String.valueOf(opStack.pop()));
                            peekOp = opStack.peek();
                        }
                        opStack.push(currentOp);
                    }
                    count = 0;
                    currentIndex = i + 1;
                } else {
                    count++;
                }
            }
            if (count > 1 || (count == 1 && !isOperator(arr[currentIndex]))) {
                // 最后一个字符不是括号或者其他运算符的则加入后缀式栈中
                postfixStack.push(new String(arr, currentIndex, count));
            }

            while (opStack.peek() != ',') {
                // 将操作符栈中的剩余的元素添加到后缀式栈中
                postfixStack.push(String.valueOf(opStack.pop()));
            }
        }

        /**
         * 判断是否为算术符号
         *
         * @param c
         * @return
         */
        private boolean isOperator(char c) {
            return c == '+' || c == '-' || c == '*' || c == '(' || c == ')';
        }

        /**
         * 利用ASCII码-40做下标去算术符号优先级
         *
         * @param cur
         * @param peek
         * @return
         */
        public boolean compare(char cur, char peek) {// 如果是peek优先级高于cur，返回true，默认都是peek优先级要低
            boolean result = false;
            if (operatPriority[(peek) - 40] >= operatPriority[(cur) - 40]) {
                result = true;
            }
            return result;
        }

        /**
         * 按照给定的算术运算符做计算
         *
         * @param firstValue  第一个数
         * @param secondValue 第二个数
         * @param currentOp   操作符
         * @return 值
         */
        private String calculate(int firstValue, int secondValue, char currentOp) {
            int result = 0;
            switch (currentOp) {
                case '+':
                    result = firstValue + secondValue;
                    break;
                case '-':
                    result = firstValue - secondValue;
                    break;
                case '*':
                    result = firstValue * secondValue;
                    break;
                default:
                    break;
            }
            return String.valueOf(result);
        }
    }
}


