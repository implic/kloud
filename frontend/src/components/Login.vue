<template>
    <body id="poster">
    <el-form class="login-container" label-position="left"
             label-width="0px" >
        <h3 class="login_title">用户登录</h3>
        <el-form-item>
            <el-input type="text" v-model="loginForm.username"
                      auto-complete="off" placeholder="账号" ></el-input>
        </el-form-item>
        <el-form-item>
            <el-input type="password" v-model="loginForm.password"
                      auto-complete="off" placeholder="密码"></el-input>
        </el-form-item>
        <el-form-item style="width: 100%">
            <el-button type="primary" style="width: 100%;background: #505458;border: none" v-on:click="login">登录</el-button>
        </el-form-item>
    </el-form>
    </body>
</template>


<script>

    export default {
        name: 'Login',
        data () {
            return {
                loginForm: {
                    username: '',
                    password: ''
                },
                responseResult: []
            }
        },
        methods: {
            login () {
                // var _this = this
                this.$axios
                    .post('/login', this.loginForm)
                    .then(successResponse => {
                        if (successResponse.data &&
                            successResponse.data.login != undefined) {
                            let user = successResponse.data
                            let session = window.sessionStorage
                            session.setItem('id', user.id)
                            session.setItem('login', user.login)
                            session.setItem('realName', user.realName)
                            session.setItem('email', user.email)
                            session.setItem('userType', user.userType)
                            session.setItem('user', JSON.stringify(user))
                            this.$router.replace({path: '/home'})
                        }

                    })
                // .catch(failResponse => {
                //     alert(failResponse)
                // })
            }
        }
    }
</script>



<style>
    .login-container {
        border-radius: 15px;
        background-clip: padding-box;
        margin: 150px auto;
        width: 350px;
        padding: 35px 35px 15px 35px;
        background: #fff;
        border: 1px hidden #eaeaea;
        box-shadow: 0 0 25px rgba(135, 206, 250, 0.8);
        background-color: transparent ;
    }

    .login_title {
        margin: 0px auto 40px auto;
        text-align: center;
        color: #505458;
    }

    #poster {
        background: url("../assets/login_background.jpg") no-repeat center;
        height: 100%;
        width: 100%;
        background-size: cover;
        position: fixed;
    }

    body {
        margin: 0px;
    }
</style>
