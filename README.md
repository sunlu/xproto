# xproto
create project

类型于ProtoBuf轻量版通讯协议打包组件

支持四种数据类型 VarInt\String\Object\Array

#协议格式
{  字段数 short
  [字段ID & 字段类型, 字段值],
  [字段ID & 字段类型, 字段值],
  [字段ID & 字段类型, 字段值]
}
## 字段值说明
当字段类型为String时，字段值为 字符串长度+内容
当字段类型为Array时，字段值为数组长组+数组元素1+数组元素n
当字段类型为Object时，字段值为子类型
