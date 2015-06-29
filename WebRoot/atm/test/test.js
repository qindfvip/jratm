

$('#status').eovacombo({
	url : '/widget/comboJson/player_code-status',
	valueField : 'ID',
	textField : 'CN'
});

var crud_query = function() {
	$('#grid').datagrid('load', sy.serializeObject($('#queryForm')));
};

$('#queryForm').keydown(function(event) {
	if (event.keyCode == 13) {
		crud_query();
	}
});
var crud_add = function() {
	var grid = $('#grid');
	loadDialog(grid, '添加玩家信息', '/singleGrid/toAdd/biz_demo_users');
};
var crud_update = function() {
	var grid = $('#grid');
	var row = grid.datagrid('getSelected');
	var array = grid.datagrid('getSelections');
	var num = array.length;//获取要删除信息的个数
	if (num > "1") {
		slideMsg("请勿选择多行数据！");
		return;
	}
	if (isSelected(row)) {
		//id
		loadDialog(grid, '修改玩家信息', '/singleGrid/toUpdate/biz_demo_users-' + row.pk_val);
	}
};
var x = $("#eova-query").width();
console.log(x);
var max = parseInt(x / 290);
console.log('每行最多能显示：' + max);
if (max != 0) {
	var count = 4;
	var zs = parseInt(count / max);
	var ys = count % max;
	if (ys > 0) {
		zs++;
	}
	// 计算完整显示所需高度
	var y = 40 + 25 * zs;
	$("#eova-query").parent().parent().css("height", y + "px");
	//console.log('ys'+ ys);
	//console.log('共需：'+ zs);
	//console.log('height：'+ y);
}
var crud_deletes = function() {
	var grid = $('#grid');
	var array = grid.datagrid('getSelections');
	var id2 = "";
	var num = array.length; //获取要删除信息的个数
	if (num == "0") {
		slideMsg("请选择一条记录！");
		return;
	}
	for ( var i = 0; i < array.length; i++) { //组成一个字符串，ID主键用逗号隔开  
		id2 = id2 + array[i].pk_val + ",";
	}
	confirmDel(grid, '/singleGrid/delete/biz_demo_users-' + id2);
};
$(function() {
	var selectIndex;
	var gridId = "#grid";
	var myGrid = $(gridId).datagrid({
		columns : [ [ {
			field : 'id',
			title : 'ID',
			sortable : true,
			width : 130
		}, {
			field : 'nickName',
			title : '艺人姓名',
			sortable : true,
			formatter : function(value, row, index, field) {
				if (value && value.length > 10) {
					return '<span title="' + value + '">' + value + '</span>';
				}
				return value;
			},
			width : 130
		}, {
			field : 'status',
			title : '状态',
			sortable : true,
			editor : {
				type : 'eovacombo',
				options : {
					url : '/widget/comboJson/player_code-status',
					valueField : 'ID',
					textField : 'CN'
				}
			},
			width : 130
		}, {
			field : 'loginId',
			title : '登录账户',
			sortable : true,
			formatter : function(value, row, index, field) {
				if (value && value.length > 10) {
					return '<span title="' + value + '">' + value + '</span>';
				}
				return value;
			},
			editor : {
				type : 'eovatext',
				options : {
					url : '/widget/comboJson/player_code-loginId',
					valueField : 'ID',
					textField : 'CN'
				}
			},
			width : 130
		}, {
			field : 'regTime',
			title : '注册时间',
			sortable : true,
			width : 180
		}, {
			field : 'info',
			title : '备注',
			sortable : true,
			width : 130
		}, ] ],
		onHeaderContextMenu : function(e, field) {
			e.preventDefault();
			if (!cmenu) {
				createColumnMenu();
			}
			cmenu.menu('show', {
				left : e.pageX,
				top : e.pageY
			});
		},
		onRowContextMenu : function(e, rowIndex, rowData) {
			e.preventDefault();
			if (!rowMenu) {
				createRowMenu();
			}
			selectIndex = rowIndex;
			rowMenu.menu('show', {
				left : e.pageX,
				top : e.pageY
			});
		}
	});

	var slideMsg = function(str, $pjq) {
		$pjq.messager.show({
			title : '操作提示',
			msg : str,
			timeout : 1500,
			showType : 'slide'
		});
	};

	// 开启编辑模式
	myGrid.datagrid('enableCellEditing');

	var rowMenu;
	function createRowMenu() {
		rowMenu = $('<div/>').appendTo('body');
		rowMenu.menu({
			id : 'rowMenu',
			onClick : function(item) {
				console.log('click menu' + item.text);
			}
		});
		rowMenu.menu('appendItem', {
			text : '刷新',
			name : 'reload',
			iconCls : 'pagination-load',
			onclick : function() {
				myGrid.datagrid('reload');
			}
		});
		rowMenu.menu('appendItem', {
			text : '导出所有数据',
			name : 'exportAll',
			iconCls : 'icon-pageexcel',
			onclick : function() {
				window.location.href = '/grid/export/player_code';
			}
		});

		rowMenu.menu('appendItem', {
			text : '新增行',
			name : 'add',
			iconCls : 'icon-tableadd',
			onclick : function() {
				myGrid.datagrid('insertRow', {
					index : 0,
					row : {}
				});
			}
		});
		rowMenu.menu('appendItem', {
			text : '删除行',
			name : 'delete',
			iconCls : 'icon-tabledelete',
			onclick : function() {
				// var row = myGrid.datagrid('getSelected');
				// index = myGrid.datagrid('getRowIndex', row);
				console.log('删除行，索引=' + selectIndex);
				myGrid.datagrid('deleteRow', selectIndex);
			}
		});
		rowMenu.menu('appendItem', {
			text : '保存数据',
			name : 'save',
			iconCls : 'icon-tablesave',
			onclick : function() {
				var inserted = myGrid.datagrid('getChanges', 'inserted');
				var deleted = myGrid.datagrid('getChanges', 'deleted');
				var updated = myGrid.datagrid('getChanges', 'updated');

				var isOk = true;

				var json1 = JSON.stringify(inserted);
				console.log('保存add数据' + json1);
				$.mypost('/grid/add/player_code', {
					rows : json1
				}, function(result, status) {
					if (!result.success) {
						isOk = false;
					}
				}, false);
				var json2 = JSON.stringify(deleted);
				console.log('保存delete数据' + json2);
				$.mypost('/grid/delete/player_code', {
					rows : json2
				}, function(result, status) {
					if (!result.success) {
						isOk = false;
					}
				}, false);
				var json3 = JSON.stringify(updated);
				console.log('保存update数据' + json3);
				$.mypost('/grid/update/player_code', {
					rows : json3
				}, function(result, status) {
					if (!result.success) {
						isOk = false;
					}
				}, false);

				if (isOk) {
					slideMsg("保存成功！", $);
				} else {
					$.messager.alert('提示', result.msg, 'error');
				}

				myGrid.datagrid('acceptChanges');
				console.log('标记更改');
			}
		});
		rowMenu.menu('appendItem', {
			text : '回滚数据',
			name : 'reject',
			iconCls : 'icon-undo',
			onclick : function() {
				myGrid.datagrid('rejectChanges');
				console.log('回滚数据');
			}
		});
		rowMenu.menu('appendItem', {
			text : '其它功能',
			name : 'other',
			onclick : function() {
				alert('Eova is So Easy');
			}
		});
	}

	var cmenu;
	function createColumnMenu() {
		cmenu = $('<div/>').appendTo('body');
		cmenu.menu({
			// 点击隐藏显示列
			onClick : function(item) {
				if (item.iconCls == 'icon-ok') {
					myGrid.datagrid('hideColumn', item.name);
					cmenu.menu('setIcon', {
						target : item.target,
						iconCls : 'icon-empty'
					});
				} else {
					myGrid.datagrid('showColumn', item.name);
					cmenu.menu('setIcon', {
						target : item.target,
						iconCls : 'icon-ok'
					});
				}
			}
		});
		// 动态加载列作为菜单项目
		var fields = myGrid.datagrid('getColumnFields');
		for ( var i = 0; i < fields.length; i++) {
			var field = fields[i];
			var col = myGrid.datagrid('getColumnOption', field);
			cmenu.menu('appendItem', {
				text : col.title,
				name : field,
				iconCls : 'icon-ok'
			});
		}
	}

});

$(function() {
	$('div[class="eova-text"]').eovatext();
	$('div[class="eova-find"]').eovafind();
	$('div[class="eova-auto"]').eovaauto();
	$('div[class="eova-time"]').eovatime();
	$('div[class="eova-icon"]').eovaicon();
	$('div[class="eova-combo"]').eovacombo();
	$('div[class="eova-check"]').eovacheck();
});
