import { Button, Form, Input, message, Modal } from 'antd'
import React, { useState } from 'react';
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { login } from '../utils'

// button, 点完之后会弹窗，form里填username password
// 先定义一个func Login,把ui搭建出来，button，点button出来modal,设置display modal ... 
function Login({ onSuccess }) { // { onSuccess }是props, props包含什么由调用他的决定比如<Login a={b}>，就和func call一样
  const [displayModal, setDisplayModal] = useState(false) // modal状态 一开始不现实
 
  const handleCancel = () => {
    setDisplayModal(false)
  }
 
  const signinOnClick = () => {
    setDisplayModal(true)
  }
 
  const onFinish = (data) => {
    // utils一般都会用到 .then func
    login(data)
      .then((data) => {
        setDisplayModal(false)
        message.success(`Welcome back, ${data.name}`)
        onSuccess() //登陆成功之后执行onSuccess，也就是app.js里的signinOnSuccess
      }).catch((err) => {
        message.error(err.message)
      })
  }
 
  return (
    <>
      <Button shape="round" onClick={signinOnClick} style={{ marginRight: '20px' }}>
        Login
      </Button>
      {/* 点了login之后弹出modal */}
      <Modal
        title="Log in"
        visible={displayModal}
        onCancel={handleCancel} // 关掉
        footer={null}
        destroyOnClose={true}
      >
        <Form
          name="normal_login"
          onFinish={onFinish}
          preserve={false}
        >
          {/* 为什么onFinish和form绑一起不和button onclick绑一起
          因为form绑一起会把form里信息打包发过去
          buttom和form之间有trigger，是htmlType="submit" */}
          <Form.Item
            name="user_id"
            rules={[{ required: true, message: 'Please input your Username!' }]}
          >
            <Input prefix={<UserOutlined />} placeholder="Username" />
          </Form.Item>
          <Form.Item
            name="password"
            rules={[{ required: true, message: 'Please input your Password!' }]}
          >
            <Input.Password
              prefix={<LockOutlined />}
              placeholder="Password"
            />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit">
              Login
            </Button>
          </Form.Item>
        </Form>
      </Modal>
    </>
  )
}
 
export default Login