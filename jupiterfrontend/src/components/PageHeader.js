import { Layout, Row, Col, Button } from 'antd'
import Favorites from './Favorites'
import Register from './Register'
import Login from './Login'
import React from 'react'
 
const { Header } = Layout
 
function PageHeader({ loggedIn, signoutOnClick, signinOnSuccess, favoriteItems })  {
  return (
    <Header>
      <Row justify='space-between'>
        {/* 两边顶行 */}
        <Col>
          {loggedIn && <Favorites favoriteItems={favoriteItems} />}
        </Col>
        <Col>
          {loggedIn && 
            <Button shape="round" onClick={signoutOnClick}>
                Logout
            </Button>}
            {/* 此处类似if else 登陆了or没登陆,才显示 */}
          {!loggedIn && (
            <>
              <Login onSuccess={signinOnSuccess} />
              {/* 登陆成功，app把signinonsuccess传下来，给Login调用 */}
              <Register />
            </>
          )}
        </Col>
      </Row>
    </Header>
  )
}
 
export default PageHeader