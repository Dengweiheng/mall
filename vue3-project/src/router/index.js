import { createRouter, createWebHistory } from 'vue-router'

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue'),
        meta: { title: '登录' }
    },
    {
        path: '/',
        component: () => import('../layout/index.vue'),
        redirect: '/product/list',
        children: [
            {
                path: 'home',
                name: 'Home',
                component: () => import('../views/Home.vue'),
                meta: { title: '首页' }
            },
            {
                path: 'product/list',
                name: 'ProductList',
                component: () => import('../views/product/List.vue'),
                meta: { title: '商品中心', public: true }
            },
            {
                path: 'product/cart',
                name: 'Cart',
                component: () => import('../views/product/Cart.vue'),
                meta: { title: '购物车' }
            },
            {
                path: 'order/manage',
                name: 'OrderManage',
                component: () => import('../views/order/OrderManage.vue'),
                meta: { title: '订单审核' }
            },
            {
                path: 'order/detail/:id',
                name: 'OrderDetail',
                component: () => import('../views/order/OrderDetail.vue'),
                meta: { title: '订单详情', hideMenu: true }
            },
            {
                path: 'product/manage',
                name: 'ProductManage',
                component: () => import('../views/product/ProductManage.vue'),
                meta: { title: '商品管理' }
            },
            {
                path: 'product/stock',
                name: 'StockManage',
                component: () => import('../views/product/StockManage.vue'),
                meta: { title: '库存管理' }
            },
            {
                path: 'order/my',
                name: 'MyOrder',
                component: () => import('../views/order/MyOrder.vue'),
                meta: { title: '我的订单' }
            },
            {
                path: 'profile',
                name: 'Profile',
                component: () => import('../views/profile/index.vue'),
                meta: { title: '个人中心' }
            },
            {
                path: 'admin-manage',
                name: 'AdminManage',
                component: () => import('../views/admin/AdminManage.vue'),
                meta: { title: '管理员管理' }
            },
            {
                path: 'finance/report',
                name: 'FinanceReport',
                component: () => import('../views/finance/Report.vue'),
                meta: { title: '财务报表' }
            },
            {
                path: 'product/category',
                name: 'CategoryManage',
                component: () => import('../views/product/CategoryManage.vue'),
                meta: { title: '分类管理' }
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
    const user = localStorage.getItem('erp-user')
    // 公开页面直接放行（商品列表、登录页）
    if (to.meta.public || to.path === '/login') {
        next()
    } else if (!user) {
        // 未登录访问需权限的页面，跳到商品列表（首页展示）
        next('/product/list')
    } else {
        next()
    }
})

export default router
