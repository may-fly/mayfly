filename=%s
filetype=$(file -b $filename)
containText=$(echo "$filetype" | grep -E "text|empty")
if [ "$containText" != "" ] ;then
 cat "$filename"
else
  echo "该文件不是文本文件，不可读取其内容"
fi