/**
 * 产品模块
 */
var Products = {};

/**
 * 各个模块
 */
Products.module = {};
/**
 * 网点管理
 */
Products.module.branch = {
    name: "网点管理",
    id: '40288e9f48a123e80148a1240d1f0000',
    type: 3
}
/**
 * 网点管理
 */
Products.module.region = {
    name: "区划管理",
    id: '40288e9f48448d8a0148448d90770004',
    type: 3
}
/**
 * 网点管理
 */
Products.module.fendan = {
    name: "分单管理",
    id: '40288e9f48a123e80148a1240e210001',
    type: 2
}
/**
 * 分单管理--批量分单
 */
Products.module.fendan_batch = {
    name: "批量分单",
    id: '40288e9f48a123e80148a1240e2b0003',
    type: 0
}
/**
 * 分单管理--订单管理
 */
Products.module.fendan_ordermge = {
    name: "订单管理",
    id: '40288e9f48a123e80148a1240e260002',
    type: 0
}
/**
 * 分单管理--子项
 */
Products.module.fendan.kids = [
    Products.module.fendan_batch,
    Products.module.fendan_ordermge
]


/**
 * 车辆管理
 */
Products.module.cars = {
    id: "40288e9f48448d8a0148448d90720003",
    name: "车辆管理",
    type: 2
}
/**
 * 车辆管理--车辆监控
 */
Products.module.cars_control = {
    id: "40288e9f48afcbb40148afcbbfb10000",
    name: "车辆监控",
    type: 0
}
/**
 * 车辆管理--车辆调度
 */
Products.module.cars_dispatch = {
    id: "40288e9f48afcaf60148afcb01690000",
    name: "车辆调度",
    type: 0
}
/**
 * 车辆管理--司机管理
 */
Products.module.cars_drivermge = {
    id: "40288e9f48afc55a0148afc56a2a0000",
    name: "司机管理",
    type: 0
}
/**
 * 车辆管理--车辆管理
 */
Products.module.cars_carsmge = {
    id: "40288e9f48afc7350148afc740600000",
    name: "车辆管理",
    type: 0
}
/**
 * 车辆管理--车辆报警处理
 */
Products.module.cars_alert = {
    id: "40288e9f48afcd530148afcd62f20000",
    name: "车辆报警处理",
    type: 0
}
/**
 * 车辆管理--车辆围栏设置
 */
Products.module.cars_fence = {
    id: "40288e9f48afccfa0148afcd046f0000",
    name: "车辆围栏设置",
    type: 0
}
/**
 * 车辆管理--车辆围栏设置
 */
Products.module.cars_overspeed = {
    id: "40288e9f48afccb50148afccbf720000",
    name: "超速报警设置",
    type: 0
}
/**
 * 车辆管理--车辆报警统计
 */
Products.module.cars_alert_statistics = {
    name: "车辆报警统计",
    id: "40288e9f48afcf5c0148afcf67020000",
    type: 0
}
/**
 * 车辆管理--车辆行驶轨迹
 */
Products.module.cars_route_trace = {
    name: "车辆行驶轨迹",
    id: "40288e9f48afcfa40148afcfaea10000",
    type: 0
}
/**
 * 车辆管理--车辆调度信息
 */
Products.module.cars_dispatch_info = {
    name: "车辆调度信息",
    id: "40288e9f48afcfff0148afd00a420000",
    type: 0
}
/**
 * 车辆管理--车辆油耗分析
 */
Products.module.cars_oil_analyst = {
    name: "车辆油耗分析",
    id: "40288e9f48afd18e0148afd1990f0000",
    type: 0
}
/**
 * 车辆管理--车辆里程统计
 */
Products.module.cars_mileage_statistics = {
    name: "车辆里程统计",
    id: "40288e9f48afd26d0148afd2774c0000",
    type: 0
}
/**
 * 车辆管理--子项
 */
Products.module.cars.kids = [
    Products.module.cars_control,
    Products.module.cars_dispatch,
    Products.module.cars_drivermge,
    Products.module.cars_carsmge,
    Products.module.cars_alert,
    Products.module.cars_fence,
    Products.module.cars_overspeed,
    Products.module.cars_alert_statistics,
    Products.module.cars_route_trace,
    Products.module.cars_dispatch_info,
    Products.module.cars_oil_analyst,
    Products.module.cars_mileage_statistics
]

/**
 * 路线规划
 */
Products.module.path = {
    name: "路线规划",
    id: '40288e9f48afca090148afca166a0000',
    type: 3
}
/**
 * 巡店管理
 */
Products.module.xundian = {
    name: "巡店管理",
    id: 'f9a8d66849a37433014a03f6d7570000',
    type: 2
}
/**
 * 巡店管理 - 计划审批
 */
Products.module.xundian_plan = {
    name: "计划审批",
    id: 'f9a8d66849a37433014a03f84f2e0001',
    type: 0
}
/**
 * 巡店管理 - 计划审批
 */
Products.module.xundian_list = {
    name: "巡店列表",
    id: 'f9a8d66849a37433014a03f99eac0002',
    type: 0
}
/**
 * 巡店管理 - 人员定位
 */
Products.module.xundian_person_locate = {
    name: "人员定位",
    id: 'f9a8d66849a37433014a03fadddf0005',
    type: 0
}
/**
 * 巡店管理 - 门店管理
 */
Products.module.xundian_storesmge = {
    name: "门店管理",
    id: 'f9a8d66849a37433014a03fa809a0004',
    type: 0
}
/**
 * 巡店管理 - 人员管理
 */
Products.module.xundian_personmge = {
    name: "人员管理",
    id: 'f9a8d66849a37433014a03fa19870003',
    type: 0
}
/**
 * 巡店管理 - 子项
 */
Products.module.xundian.kids = [
    Products.module.xundian_plan,
    Products.module.xundian_list,
    Products.module.xundian_person_locate,
    Products.module.xundian_storesmge,
    Products.module.xundian_personmge        
]



/**
 * 统计分析
 */
Products.module.tongji = {
    id: "40288e9f48afce3c0148afce46700000",
    name: "统计分析",
    type: 2
}
/**
 * 统计分析 - 线路规划对比
 */
Products.module.tongji_path = {
    name: "线路规划对比",
    id: "40288e9f48afd11a0148afd125770000",
    type: 0
}
/**
 * 统计分析 - 订单量统计
 */
Products.module.tongji_orders = {
    name: "订单量统计",
    id: "40288e9f48afcef70148afcf01c40000",
    type: 0
}
/**
 * 统计分析 - 区划量统计
 */
Products.module.tongji_regions = {
    name: "区划量统计",
    id: "8a04a77b4fdf00350150da70b8240180",
    type: 0
}
/**
 * 统计分析 - 子项
 */
Products.module.tongji.kids = [
    Products.module.tongji_path,
    Products.module.tongji_orders,
    Products.module.tongji_regions
]






//产品列表
var data_products_v1 = [
    {
        name: "企业应用产品",
        list: [
            {
                id: "1",
                name: "网点管理",
                href: "",
                icon: urls.myself + "apps/images/nav/wangdiantong.png",
                kids: [
                    Products.module.branch
                ]
            },
            {
                id: "2",
                name: "区划管理",
                href: "",
                icon: urls.myself + "apps/images/nav/quhuabao.png",
                kids: [
                    Products.module.region
                ]
            },
            {
                id: "3",
                name: "分单管理",
                href: "",
                icon: urls.myself + "apps/images/nav/fendanbao.png",
                kids: [
                    Products.module.region,
                    Products.module.fendan
                ]
            },
            {
                id: "7",
                name: "车辆监控",
                href: "item/cheliang",
                icon: urls.myself + "apps/images/nav/cheliang.png",
                kids: [
                    Products.module.cars
                ]
            },
            {
                id: "4",
                name: "多路线规划",
                href: "",
                icon: urls.myself + "apps/images/nav/pathplan.png",
                kids: [
                    Products.module.region,
                    Products.module.branch,
                    Products.module.fendan,
                    Products.module.path,
                    Products.module.cars
                ]
            },
            {
                id: "5",
                name: "巡店管理",
                href: "",
                icon: urls.myself + "apps/images/nav/xundianbao.png",
                kids: [
                    Products.module.xundian
                ]
            },
            {
                id: "6",
                name: "销售管理",
                href: "",
                icon: urls.myself + "apps/images/nav/xiaoshoubao.png",
                kids: [
                    Products.module.region,
                    Products.module.branch,
                    Products.module.xundian
                ]
            },
            {
                id: "8",
                name: "考勤管理",
                href: "",
                icon: urls.myself + "apps/images/nav/kaoqin.png",
                kids: [
                    Products.module.branch,
                    Products.module.xundian
                ]
            },
            {
                id: "10",
                name: "统计分析",
                target: "_self",
                href: "",
                icon: urls.myself + "apps/images/nav/tongji.png",
                kids: [
                    Products.module.tongji
                ]
            },
            {
                id: "9",
                name: "路程计算",
                href: "javascript:void(0)",
                icon: urls.myself + "apps/images/nav/wangdian.gif",
                kids: [
                ]
            }
            /*,
            {
                id: "11",
                name: "地址通",
                href: "javascript:void(0)",
                icon: urls.myself + "apps/images/nav/wangdian.gif",
                kids: [
                ]
            }
            */
        ]
    },

    {
        name: "行业解决方案",
        list: [
            {
                id: "wuliu",
                name: "物流行业",
                href: "item/solutions/wuliu",
                icon: urls.myself + "apps/images/nav/wuliu.png",
                kids: [
                ]
            },
            {
                id: "2001",
                name: "家电行业",
                href: "javascript:void(0)",
                icon: urls.myself + "apps/images/nav/wangdian.gif",
                kids: [
                ]
            },
            {
                id: "2002",
                name: "金融行业",
                href: "javascript:void(0)",
                icon: urls.myself + "apps/images/nav/wangdian.gif",
                kids: [
                ]
            },
            {
                id: "2003",
                name: "保险行业",
                href: "javascript:void(0)",
                icon: urls.myself + "apps/images/nav/wangdian.gif",
                kids: [
                ]
            }
        ]
    },
    {
        name: "商业智能分析",
        list: [
            {
                id: "3000",
                name: "选址分析",
                href: "javascript:void(0)",
                icon: urls.myself + "apps/images/nav/wangdian.gif",
                kids: [
                ]
            },
            {
                id: "3001",
                name: "竞品分析",
                href: "javascript:void(0)",
                icon: urls.myself + "apps/images/nav/wangdian.gif",
                kids: [
                ]
            },
            {
                id: "3002",
                name: "商圈分析",
                href: "javascript:void(0)",
                icon: urls.myself + "apps/images/nav/wangdian.gif",
                kids: [
                ]
            },
            {
                id: "3003",
                name: "营销分析",
                href: "javascript:void(0)",
                icon: urls.myself + "apps/images/nav/wangdian.gif",
                kids: [
                ]
            }
        ]
    },
    {
        name: "移动应用APP",
        list: [
            {
                id: "4000",
                name: "车辆监控APP",
                href: "javascript:void(0)",
                icon: urls.myself + "apps/images/nav/wangdian.gif",
                kids: [
                ]
            },
            {
                id: "4001",
                name: "巡店APP",
                href: "javascript:void(0)",
                icon: urls.myself + "apps/images/nav/wangdian.gif",
                kids: [
                ]
            }
        ]
    }
];

var data_apps_wuliu_v1 = [
    Products.module.branch,
    Products.module.region,
    Products.module.fendan,
    Products.module.path,
    Products.module.cars,
    Products.module.tongji
];
