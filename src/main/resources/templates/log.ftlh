<#import "commons/layout.ftlh" as main>
<#assign pageScript in main>
    <script>
        function reloadTable() {
            $('#bootstrapTable').bootstrapTable('refresh', {silent: true});
        }
        $(function () {
            var triggerMap = ${triggerMap};
            $('#bootstrapTable').bootstrapTable({
                url: 'api/logs',
                // toolbar: '#toolbar',
                striped: true,  //表格显示条纹
                pagination: true, //启动分页
                pageSize: 10,  //每页显示的记录数
                pageNumber: 1, //当前第几页
                pageList: [10, 15, 20],  //记录数可选列表
                showRefresh: true,  //显示刷新按钮
                showToggle: true,
                sidePagination: "server", //表示服务端请求
                responseHandler:function(res){
                    //远程数据加载之前,处理程序响应数据格式,对象包含的参数: 我们可以对返回的数据格式进行处理
                    //在ajax后我们可以在这里进行一些事件的处理
                    var result = {
                        rows: res.data,
                        total: res.total
                    };
                    return result;
                },
                //设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
                //设置为limit可以获取limit, offset, search, sort, order
                queryParamsType : "undefined",
                columns: [{
                    title: 'ID',
                    field: 'id'
                }, {
                    title: '执行类名',
                    field: 'jobClass'
                }, {
                    title: '执行参数',
                    field: 'params'
                }, {
                    title: '执行结果',
                    field: 'success',
                    formatter: function (value) {
                        return value ? '<span class="label label-success">成功</span>' : '<span class="label label-danger">失败</span>';
                    }
                }, {
                    title: '开始时间',
                    field: 'startAt'
                }, {
                    title: '结束时间',
                    field: 'finishAt'
                }, {
                    title: '任务耗时',
                    field: 'duration',
                    formatter: function (value) {
                        var minutes = (value / (1000 * 60)).toFixed(0),
                            seconds = ((value - minutes * 1000 * 60) / 1000).toFixed(0),
                            millis = value - minutes * 1000 * 60 - seconds * 1000;
                        return minutes + '分' + seconds + '秒' + millis + '毫秒';
                    }
                }]
            });
        });

    </script>
</#assign>
<@main.layout title="调度执行日志" tab="logs">
    <h2>调度执行日志</h2>
    <div class="col-md-12">
        <table id="bootstrapTable"></table>
    </div>
</@main.layout>
