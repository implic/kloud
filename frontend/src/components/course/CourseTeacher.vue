<template>
    <div>
        <div style="text-align: left; margin-bottom: 10px;"><span
                style="margin-left: 20px;font-size: 20px;
                font-weight:
                bolder"><el-button type="text" style="font-size: 20px;
                font-weight:
                bolder"
                                   @click="$router.replace({path: '/course'})">我的课程
        </el-button>
            <span class="el-icon-arrow-right" v-if="isCourseItem()"></span>
            <span style="margin-left: 6px" v-if="isCourseItem()">详细信息</span>
        </span></div>
        <router-view/>
        <div style="background-color: white;margin: 0 10px;
        padding-top: 20px;" v-if="!isCourseItem()">
            <el-row type="flex" justify="start">
                <el-input placeholder="搜索"
                          prefix-icon="el-icon-search"
                          v-model="search" type="text"
                          style="margin-left: 25%; margin-right: 30%;"
                ></el-input>
            </el-row>
            <el-row style="margin-left: 20px">
                <el-card v-for="item in coursesShow" :key="item.id"
                         class="card_item" body-style="padding: 0px;">
                    <a href="" style="text-decoration:none; "
                       @click="getCourse(item)">
                    <div class="cover">
                       <img src="../../assets/course-cover-1.png" alt="封面">
                    </div>
                    <div class="course_title">
                        <span>{{item.courseName}}</span>
                    </div>
                    <div class="course_info">
                        <i class="iconfont icon-xueqi"></i>
                        <span class="course_semester">{{item.semester}}</span>
                        <i class="iconfont icon-zongrenshu"></i>
                        <span class="course_sum">{{item.total}}人</span>
                    </div>
                    </a>
                </el-card>
                <el-card class="card_item card_add" body-style="padding: 0px;">
                    <div class="course_add">
                        <el-button type="text" @click="dialogVisible = true">
                        <i class="iconfont icon-add-sy"></i>
                        <br>
                        <span style="color: #9B9DA2;">新建课程
                        </span>
                        </el-button>
                    </div>
                </el-card>
                <el-dialog title="添加课程" :visible.sync="dialogVisible" width="30%"
                           :center="false" :append-to-body="true"
                           >
                    <el-input v-model="courseAddForm.courseName"
                              placeholder="请输入课程名称"></el-input>
                    <div style="margin-top: 20px; text-align:center;">
                    <el-button type="primary" @click="submit"
                               >提交
                    </el-button>
                    <el-button type="info" style="margin-left: 20px;"
                               @click="cancel">取消
                    </el-button>
                    </div>
                </el-dialog>
            </el-row>
        </div>
    </div>
</template>

<script>


    export default {
        name: "CourseTeacher",
        data() {
            return {
                dialogVisible: false,
                courses: [],
                courseAddForm: {
                    id: '',
                    realName: '',
                    courseName: '',
                },
                search: '',
                show: true,
                courseItem: ''
            }
        },
        computed: {
            coursesShow: {
                get() {
                    let search = this.search
                    if(search === '' || search == null ) {
                        return this.courses
                    }
                    else return this.courses.filter(course => {
                        return course.courseName.indexOf(search) !== -1 ||
                            course.semester.indexOf(search) !== -1
                    })
                },

                set(v) {
                    this.show = v
                }

            }
        },
        mounted() {
            let _this = this
            let user = JSON.parse(window.sessionStorage.getItem('user'))
            let id = user.id
            this.$axios.post('/course/listCourses', {"id":id})
                .then(successResponse => {
                    _this.courses = successResponse.data
                    _this.coursesShow = _this.courses
                })
            _this.courseAddForm.id = user.id
            _this.courseAddForm.realName = user.realName
        },
        methods: {
            submit() {
                if(this.courseAddForm.courseName === '') {
                    alert('课程名不能为空')
                }
                else {
                    this.$axios.post('/course/addCourse', this.courseAddForm)
                        .then(() => {
                            this.dialogVisible = false
                            window.location.reload()
                        })
                }
            },
            cancel() {
                this.courseName = ''
                this.dialogVisible = false
            },
            getCourse(item) {
                window.sessionStorage.setItem('courseName', item.courseName)
                window.sessionStorage.setItem('semester', item.semester)
                this.$router.replace({path: "/course/" + item.id})
            },
            isCourseItem() {
                var reg = /^\/course\/\d+#?$/
                return reg.test(this.$route.path)
            }
        }
    }
</script>

<style scoped>
    img {
        width: 268px;
        height: 150px;
        margin: 0 auto;
    }
    .card_item {
        width: 270px;
        height: 233px;
        float: left;
        margin-right: 20px;
        margin-bottom: 20px;
    }
    .course_title {
        font-size: 18px;
        color: #2c3e50;
        text-align: left;
        padding-top: 3px;
        padding-left: 5px;
    }

    .course_info {
        margin-top: 8px;
        margin-left: 5px;
        text-align: left;
        color: #9B9DA2;
    }

    .icon-xueqi, .icon-zongrenshu {
        font-size: 18px;
        padding-right: 2px;
    }

    .icon-zongrenshu {
        padding-left: 25px;
    }

    .card_add {

    }

    .course_add {
        padding-top: 30px;
    }

    .icon-add-sy {
        font-size: 100px;
        color: #9B9DA2;
    }

</style>