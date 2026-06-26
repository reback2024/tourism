/** 邮箱校验 */
export function validateEmail(rule, value, callback) {
  const emailReg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
  if (value && !emailReg.test(value)) {
    callback(new Error('请输入正确的邮箱格式'))
  } else {
    callback()
  }
}

/** 手机号校验 */
export function validatePhone(rule, value, callback) {
  const phoneReg = /^1[3-9]\d{9}$/
  if (value && !phoneReg.test(value)) {
    callback(new Error('请输入正确的手机号'))
  } else {
    callback()
  }
}
