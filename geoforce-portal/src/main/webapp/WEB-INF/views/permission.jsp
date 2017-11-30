<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="resources/css/kzt.css">
    <link rel="stylesheet" type="text/css" href="resources/css/consolePerson.css">
    <title>控制台|近一个月消费情况开放平台</title>
</head>
<body>
<div class="main-page">
    <nav class="cd-side-navigation">
        <ul>
            <li>
                <a href="#0" data-menu="console">
                    <img src="resources/img/img_kzt_wdyy.png" alt="">我的应用
                </a>
            </li>
            <li>
                <a href="#0" class="selected" data-menu="permission">
                    <img src="resources/img/img_kzt_qxtj.png" alt="">权限统计
                </a>
            </li>
            <li>
                <a href="#0" data-menu="consolePerson">
                    <img src="resources/img/img_kzt_grzl.png" alt="">个人资料
                </a>
            </li>
        </ul>
    </nav>
    <div class="cd-main">
        <section class="cd-section services visible">
            <div class="con-right">
                <div class="api">
                    <div class="header">
                        <span>地址解析API</span>
                        <aside>
                            <a href="#" class="apply">申请配额</a>
                            <a href="faq.html" class="help">帮助文档</a>
                        </aside>
                    </div>
                    <div class="con">
                        <section class="left">
                            <!--<p>近一月使用流量</p>-->
                            <div id="container11" style="min-width:600px;height:300px"></div>
                        </section>
                        <section class="right">
                            <div class="top">
                                <p>日配额使用率</p>
                                <div class="aside">
                                    <div class="cart" id="container12" style="min-width: 130px;height: 130px"></div>
                                    <div class="word">
                                        <p>已使用：10万次</p>
                                        <p>日配额：<span>1000万次</span></p>
                                    </div>
                                </div>
                            </div>
                            <div class="bottom">
                                <p>使用情况</p>
                                <div>
                                    <span class="Normal"><i class="active"></i>正常</span>
                                    <span class="ABnormal"><i></i>警告</span>
                                </div>
                            </div>
                        </section>
                    </div>
                </div>
                <div class="api">
                    <div class="header">
                        <span>面管理API</span>
                        <aside>
                            <a href="#" class="apply">申请配额</a>
                            <a href="faq.html" class="help">帮助文档</a>
                        </aside>
                    </div>
                    <div class="con">
                        <section class="left">
                            <!--<p>近一月使用流量</p>-->
                            <div id="container21" style="min-width:600px;height:300px"></div>
                        </section>
                        <section class="right">
                            <div class="top">
                                <p>日配额使用率</p>
                                <div class="aside">
                                    <div class="cart" id="container22" style="min-width: 130px;height: 130px"></div>
                                    <div class="word">
                                        <p>已使用：10万次</p>
                                        <p>日配额：<span>1000万次</span></p>
                                    </div>
                                </div>
                            </div>
                            <div class="bottom">
                                <p>使用情况</p>
                                <div>
                                    <span class="Normal"><i></i>正常</span>
                                    <span class="ABnormal"><i class="active"></i>警告</span>
                                </div>
                            </div>
                        </section>
                    </div>
                </div>
                <div class="api">
                    <div class="header">
                        <span>POI服务API</span>
                        <aside>
                            <a href="#" class="apply">申请配额</a>
                            <a href="faq.html" class="help">帮助文档</a>
                        </aside>
                    </div>
                    <div class="con">
                        <section class="left">
                            <!--<p>近一月使用流量</p>-->
                            <div id="container31" style="min-width:600px;height:300px"></div>
                        </section>
                        <section class="right">
                            <div class="top">
                                <p>日配额使用率</p>
                                <div class="aside">
                                    <div class="cart" id="container32" style="min-width: 130px;height: 130px"></div>
                                    <div class="word">
                                        <p>已使用：10万次</p>
                                        <p>日配额：<span>1000万次</span></p>
                                    </div>
                                </div>
                            </div>
                            <div class="bottom">
                                <p>使用情况</p>
                                <div>
                                    <span class="Normal"><i class="active"></i>正常</span>
                                    <span class="ABnormal"><i></i>警告</span>
                                </div>
                            </div>
                        </section>
                    </div>
                </div>
                <div class="api">
                    <div class="header">
                        <span>行政边界API</span>
                        <aside>
                            <a href="#" class="apply">申请配额</a>
                            <a href="faq.html" class="help">帮助文档</a>
                        </aside>
                    </div>
                    <div class="con">
                        <section class="left">
                            <!--<p>近一月使用流量</p>-->
                            <div id="container41" style="min-width:600px;height:300px"></div>
                        </section>
                        <section class="right">
                            <div class="top">
                                <p>日配额使用率</p>
                                <div class="aside">
                                    <div class="cart" id="container42" style="min-width: 130px;height: 130px"></div>
                                    <div class="word">
                                        <p>已使用：10万次</p>
                                        <p>日配额：<span>1000万次</span></p>
                                    </div>
                                </div>
                            </div>
                            <div class="bottom">
                                <p>使用情况</p>
                                <div>
                                    <span class="Normal"><i class="active"></i>正常</span>
                                    <span class="ABnormal"><i></i>警告</span>
                                </div>
                            </div>
                        </section>
                    </div>
                </div>
            </div>
        </section>
    </div>
    <div id="cd-loading-bar" data-scale="1" class="permission"></div> <!-- lateral loading bar -->
</div>
<script src="resources/lib/jquery_1.11.3/jquery.min.js"></script>
<script src="resources/lib/console/velocity.min.js"></script>
<script src="resources/lib/console/main.js"></script> <!-- Resource jQuery -->
<script src="resources/js/perChart.js" type="text/javascript"></script>
<script>
    /*近一月使用流量区域图*/
/*    $(function () {
        Highcharts.setOptions({
            timezoneOffset: -8
        });
        $.getJSON('http://datas.org.cn/jsonp?filename=json/usdeur.json&callback=?', function (data) {
            $('#container11').highcharts({
                chart: {
                    zoomType: 'x'
                },
                title: {
                    text: '近一个月消费情况'
                },
                xAxis: {
                    type: 'datetime',
                    dateTimeLabelFormats: {
                        millisecond: '%H:%M:%S.%L',
                        second: '%H:%M:%S',
                        minute: '%H:%M',
                        hour: '%H:%M',
                        day: '%m-%d',
                        week: '%m-%d',
                        month: '%Y-%m',
                        year: '%Y'
                    }
                },
                tooltip: {
                    dateTimeLabelFormats: {
                        millisecond: '%H:%M:%S.%L',
                        second: '%H:%M:%S',
                        minute: '%H:%M',
                        hour: '%H:%M',
                        day: '%Y-%m-%d',
                        week: '%m-%d',
                        month: '%Y-%m',
                        year: '%Y'
                    }
                },
                yAxis: {
                    title: {
                        text: null
                    }
                },
                legend: {
                    enabled: false
                },
                plotOptions: {
                    area: {
                        fillColor: {
                            linearGradient: {
                                x1: 0,
                                y1: 0,
                                x2: 0,
                                y2: 1
                            },
                            stops: [
                                [0, Highcharts.getOptions().colors[0]],
                                [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                            ]
                        },
                        marker: {
                            radius: 2
                        },
                        lineWidth: 1,
                        states: {
                            hover: {
                                lineWidth: 1
                            }
                        },
                        threshold: null
                    }
                },
                series: [{
                    type: 'area',
                    name: '已消费',
                    data: data
                }],
                credits:{
                    enabled:false // 禁用版权信息
                }
            });
        });
    });*/
    $(function () {
        $('#container11').highcharts({
            chart: {
                type: 'area'
            },
            title: {
                text: "近一个月消费情况"
            },
            xAxis: {
            type: 'datetime',
                dateTimeLabelFormats: {
                    day: '%y-%m-%e'
                }
            },
            series: [{
                name:"已消费",
                data: [29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4,148.5, 216.4, 194.1, 95.6, 54.4,148.5, 216.4],
                pointStart: Date.UTC(2016, 10, 22),
                pointInterval: 24 * 3600 * 1000 // one day
            }],
            yAxis: {
                title: {
                    text: null
                },
                labels: {
                    formatter: function() {
                        return this.value;
                    }
                }
            },
            tooltip: {
                dateTimeLabelFormats:{
                    day:'%Y-%m-%e'
                },
                pointFormat: '{series.name} <b>{point.y:,.0f}</b>'+"次"
            },
            plotOptions: {
                area: {
                    pointStart: 2016,
                    marker: {
                        enabled: false,
                        symbol: 'circle',
                        radius: 2,
                        states: {
                            hover: {
                                enabled: true
                            }
                        }
                    }
                }
            },
            credits:{
                enabled:false // 禁用版权信息
            }
        });
    });

    /*使用次数饼状图*/
    $(function () {
        $('#container12').highcharts({
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: null
            },
            tooltip: {
                pointFormat: '<b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                }
            },
            series: [{
                type: 'pie',
//                name: 'Browser share',
                data: [
                    {
                        name: '未使用',
                        y: 12.8,
                        sliced: true,
                        selected: true
                    },
                    ['已使用',     1.2]
                ],
                showInLegend: false // 设置为 false 即为不显示在图例中
            }],
            credits:{
                enabled:false // 禁用版权信息
            }
        });
    });


    /*近一月使用流量区域图*/
    $(function () {
        $('#container21').highcharts({
            chart: {
                type: 'area'
            },
            title: {
                text: "近一个月消费情况"
            },
            xAxis: {
                type: 'datetime',
                dateTimeLabelFormats: {
                    day: '%y-%m-%e'
                }
            },
            series: [{
                name:"已消费",
                data: [29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4,148.5, 216.4, 194.1, 95.6, 54.4,148.5, 216.4],
                pointStart: Date.UTC(2016, 10, 22),
                pointInterval: 24 * 3600 * 1000 // one day
            }],
            yAxis: {
                title: {
                    text: null
                },
                labels: {
                    formatter: function() {
                        return this.value;
                    }
                }
            },
            tooltip: {
                dateTimeLabelFormats:{
                    day:'%Y-%m-%e'
                },
                pointFormat: '{series.name} <b>{point.y:,.0f}</b>'+"次"
            },
            plotOptions: {
                area: {
                    pointStart: 2016,
                    marker: {
                        enabled: false,
                        symbol: 'circle',
                        radius: 2,
                        states: {
                            hover: {
                                enabled: true
                            }
                        }
                    }
                }
            },
            credits:{
                enabled:false // 禁用版权信息
            }
        });
    });

    /*使用次数饼状图*/
    $(function () {
        $('#container22').highcharts({
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: null
            },
            tooltip: {
                pointFormat: '<b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                }
            },
            series: [{
                type: 'pie',
//                name: 'Browser share',
                data: [
                    {
                        name: '未使用',
                        y: 12.8,
                        sliced: true,
                        selected: true
                    },
                    ['已使用',     7.2]
                ],
                showInLegend: false // 设置为 false 即为不显示在图例中
            }],
            credits:{
                enabled:false // 禁用版权信息
            }
        });
    });

    /*近一月使用流量区域图*/
    $(function () {
        $('#container31').highcharts({
            chart: {
                type: 'area'
            },
            title: {
                text: "近一个月消费情况"
            },
            xAxis: {
                type: 'datetime',
                dateTimeLabelFormats: {
                    day: '%y-%m-%e'
                }
            },
            series: [{
                name:"已消费",
                data: [29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4,148.5, 216.4, 194.1, 95.6, 54.4,148.5, 216.4],
                pointStart: Date.UTC(2016, 10, 22),
                pointInterval: 24 * 3600 * 1000 // one day
            }],
            yAxis: {
                title: {
                    text: null
                },
                labels: {
                    formatter: function() {
                        return this.value;
                    }
                }
            },
            tooltip: {
                dateTimeLabelFormats:{
                    day:'%Y-%m-%e'
                },
                pointFormat: '{series.name} <b>{point.y:,.0f}</b>'+"次"
            },
            plotOptions: {
                area: {
                    pointStart: 2016,
                    marker: {
                        enabled: false,
                        symbol: 'circle',
                        radius: 2,
                        states: {
                            hover: {
                                enabled: true
                            }
                        }
                    }
                }
            },
            credits:{
                enabled:false // 禁用版权信息
            }
        });
    });

    /*使用次数饼状图*/
    $(function () {
        $('#container32').highcharts({
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: null
            },
            tooltip: {
                pointFormat: '<b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                }
            },
            series: [{
                type: 'pie',
//                name: 'Browser share',
                data: [
                    {
                        name: '未使用',
                        y: 12.8,
                        sliced: true,
                        selected: true
                    },
                    ['已使用',     9.2]
                ],
                showInLegend: false // 设置为 false 即为不显示在图例中
            }],
            credits:{
                enabled:false // 禁用版权信息
            }
        });
    });

    /*近一月使用流量区域图*/
    $(function () {
        $('#container41').highcharts({
            chart: {
                type: 'area'
            },
            title: {
                text: "近一个月消费情况"
            },
            xAxis: {
                type: 'datetime',
                dateTimeLabelFormats: {
                    day: '%y-%m-%e'
                }
            },
            series: [{
                name:"已消费",
                data: [29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4,148.5, 216.4, 194.1, 95.6, 54.4,148.5, 216.4],
                pointStart: Date.UTC(2016, 10, 22),
                pointInterval: 24 * 3600 * 1000 // one day
            }],
            yAxis: {
                title: {
                    text: null
                },
                labels: {
                    formatter: function() {
                        return this.value;
                    }
                }
            },
            tooltip: {
                dateTimeLabelFormats:{
                    day:'%Y-%m-%e'
                },
                pointFormat: '{series.name} <b>{point.y:,.0f}</b>'+"次"
            },
            plotOptions: {
                area: {
                    pointStart: 2016,
                    marker: {
                        enabled: false,
                        symbol: 'circle',
                        radius: 2,
                        states: {
                            hover: {
                                enabled: true
                            }
                        }
                    }
                }
            },
            credits:{
                enabled:false // 禁用版权信息
            }
        });
    });

    /*使用次数饼状图*/
    $(function () {
        $('#container42').highcharts({
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: null
            },
            tooltip: {
                pointFormat: '<b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                }
            },
            series: [{
                type: 'pie',
//                name: 'Browser share',
                data: [
                    {
                        name: '未使用',
                        y: 12.8,
                        sliced: true,
                        selected: true
                    },
                    ['已使用',     2.2]
                ],
                showInLegend: false // 设置为 false 即为不显示在图例中
            }],
            credits:{
                enabled:false // 禁用版权信息
            }
        });
    });
</script>
</body>
</html>