<#import "commons/layout.ftlh" as main>
<#assign pageScript in main>
    <script>
        function pauseJob(id) {
            $.ajax('api/jobs/' + id + '/pause', {
                type: 'put',
                success: successHandler
            });
        }
        function resumeJob(id) {
            $.ajax('api/jobs/' + id + '/resume', {
                type: 'put',
                success: successHandler
            });
        }
        function deleteSchedule(id) {
            layer.confirm('是否确认删除此调度？', {
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax('api/jobs/' + id, {
                    type: 'delete',
                    success: successHandler
                })
            });
        }
        function successHandler(resp) {
            if (resp.success) {
                layer.msg('操作成功');
                reloadTable();
            } else {
                layer.msg('操作失败：' + resp.msg);
            }
        }
        function reloadTable() {
            $('#bootstrapTable').bootstrapTable('refresh', {silent: true});
        }
        $(function () {
            var triggerMap = ${triggerMap};
            $('#bootstrapTable').bootstrapTable({
                url: 'api/jobs/schedules',
                striped: true,  //表格显示条纹
                pagination: false, //启动分页
                showRefresh: true,  //显示刷新按钮
                showToggle: true,
                sidePagination: "server", //表示服务端请求
                responseHandler:function(res){
                    //远程数据加载之前,处理程序响应数据格式,对象包含的参数: 我们可以对返回的数据格式进行处理
                    //在ajax后我们可以在这里进行一些事件的处理
                    return res.data;
                },
                //设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
                //设置为limit可以获取limit, offset, search, sort, order
                queryParamsType : "undefined",
                columns: [{
                    title: 'ID',
                    field: 'id'
                }, {
                    title: '名称',
                    field: 'name'
                }, {
                    title: '分组',
                    field: 'group',
                    formatter: function (value, row) {
                        return value ? value : 'DEFAULT';
                    }
                }, {
                    title: '类名',
                    field: 'className'
                }, {
                    title: '参数',
                    field: 'params'
                }, {
                    title: '计划',
                    field: 'cron',
                    formatter: function (value, row) {
                        return value ? value : (triggerMap[value] ? triggerMap[value] : '');
                    }
                }, {
                    title: '状态',
                    field: 'state',
                    formatter: function (value, row) {
                        if (value == '正常') {
                            return '<span class="label label-success">' + value + '</span>'
                        }
                        return '<span class="label label-warning">' + value + '</span>'
                    }
                }, {
                    title: '操作',
                    align: 'center',
                    field: 'operation',
                    formatter: function(value, row, index) {
                        var actions = [];
                        if (row.state == '正常') {
                            actions.push('<button class="btn btn-sm btn-primary" onclick="pauseJob(' + row.id + ')" style="margin-right: 5px"><i class="glyphicon glyphicon-pause"></i> 暂停</button>');
                        } else if (row.state == '暂停') {
                            actions.push('<button class="btn btn-sm btn-primary" onclick="resumeJob(' + row.id + ')" style="margin-right: 5px"><i class="glyphicon glyphicon-play"></i> 恢复</button>');
                        }
                        actions.push('<button class="btn btn-sm btn-danger" onclick="deleteSchedule(' + row.id + ')" style="margin-right: 5px"><i class="glyphicon glyphicon-remove"></i> 删除</button>')
                        return actions.join('')
                    }
                }]
            });
        });
    </script>
</#assign>
<@main.layout title="调度" tab="schdule">
    <h2>调度信息</h2>
    <div class="col-md-12">
        <table id="bootstrapTable"></table>
    </div>
</@main.layout>
