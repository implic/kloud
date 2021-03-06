import Vue from 'vue'
import Router from "vue-router";

import Login from "@/components/Login";
import Home from "@/components/Home";
import Course from "@/components/course/Course";
import Container from "@/components/container/Container";
import Logging from "@/components/logging/Logging";
import Private from "@/components/private/Private";
import CourseItem from "@/components/course/CourseItem";
import Terminal from "@/components/container/Terminal";
import Node from "@/components/admin/Node";
import User from "@/components/admin/User";

Vue.use(Router)

const routes = [
    {
        path: '/',
        name: 'default',
        redirect: '/login'
    },
    {
        path: '/login',
        name: 'login',
        component: Login
    },
    {
        path: '/terminal/:course/:pod',
        name: 'terminal',
        component: Terminal
    },
    {
        path: '/home',
        name: 'home',
        component: Home,
        redirect: '/course',
        children: [
            {
                path: '/course',
                name: 'course',
                component: Course,
                children: [
                    {
                        path: '/course/:id',
                        name: 'courseItem',
                        component: CourseItem
                    }
                ]
            },
            {
                path: '/container',
                name: 'container',
                component: Container
            },
            {
                path: '/node',
                name: 'node',
                component: Node
            },
            {
                path: '/logging',
                name: 'logging',
                component: Logging
            },
            {
                path: '/private',
                name: 'private',
                component: Private
            },
            {
                path: '/user',
                name: 'user',
                component: User
            }
        ]
    }
]

const router = new Router({
    mode: 'history',
    routes
})

export default router
