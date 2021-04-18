<#import "commons/layout.ftlh" as main>
<#assign pageScript in main>
    <script>
        function doJobRightNow(id) {
            $.ajax('api/jobs/' + id + '/do', {
                type: 'get',
                success: successHandler
            });
        }
        function enableJob(id) {
            doUpdateState('api/jobs/' + id + '/enable');
        }
        function disableJob(id) {
            doUpdateState('api/jobs/' + id + '/disable');
        }
        function doUpdateState(url) {
            $.ajax(url, {
                type: 'put',
                success: successHandler
            })
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
                url: 'api/jobs/infos',
                // toolbar: '#toolbar',
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
                    field: 'enable',
                    formatter: function (value, row) {
                        if (value) {
                            return '<span class="label label-success">正常</span>'
                        }
                        return '<span class="label label-warning">禁用</span>'
                    }
                }, {
                    title: '创建时间',
                    field: 'createdAt'
                }, {
                    title: '操作',
                    align: 'center',
                    field: 'operation',
                    formatter: function(value, row, index) {
                        return [
                            '<button class="btn btn-sm btn-primary" onclick="doJobRightNow(' + row.id + ')" style="margin-right: 5px"><i class="glyphicon glyphicon-flash"></i> 立即执行</button>',
                            '<button class="btn btn-sm btn-success" onclick="enableJob(' + row.id + ')" style="margin-right: 5px"><i class="glyphicon glyphicon-check"></i> 启用</button>',
                            '<button class="btn btn-sm btn-danger" onclick="disableJob(' + row.id + ')" style="margin-right: 5px"><i class="glyphicon glyphicon-ban-circle"></i> 禁用</button>'
                        ].join('');
                    }
                }]
            });
        });

    </script>
</#assign>
<@main.layout title="任务信息" tab="jobs">
    <h2>任务信息</h2>
    <div class="col-md-12">
<#--        <div id="toolbar" class="btn-group">-->
<#--            <button id="btn_add" type="button" class="btn btn-default" onclick="creatJob()">-->
<#--                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新增-->
<#--            </button>-->
<#--        </div>-->
        <table id="bootstrapTable"></table>
    </div>
    <div class="modal fade" id="edit-modal" tabindex="-1" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><i aria-hidden="true" class="fa fa-times"></i><span class="sr-only">Close</span></button>
                    <h4 class="modal-title"></h4>
                </div>
                <div class="modal-body">
                    <div class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-3 control-label">名称</label>
                            <div class="col-sm-6">
                                <input id="grade" type="text"  class="form-control" v-model.number="clazz.grade" autofocus placeholder="输入年级（阿拉伯数字）" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label">班：</label>
                            <div class="col-sm-6">
                                <input id="classNo" type="text"  class="form-control" v-model.number="clazz.classNo" placeholder="输入班编号（阿拉伯数字）" />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal" >取消</button>
                    <button type="button" class="btn btn-primary" onclick="save()">保存</button>
                </div>
            </div>
        </div>
    </div>
</@main.layout>
