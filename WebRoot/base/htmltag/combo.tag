<div id="${id!}" name="${(isQuery!)=='true' ? QUERY+name:name}" value="${value!}" class="eova-combo"
data-options="required : ${isNoN!false} ${!isEmpty(options!) ? ', ' + options : '' }"></div>
<script>
$('#${id!}').eovacombo({
    url: '${ADMIN_PATH}/widget/comboJson/${code}-${field}',
    valueField : 'ID',
    textField : 'CN'
});
</script>