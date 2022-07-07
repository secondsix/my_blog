<template>
  <div style="margin: 20px 20px">
    <!-- 添加 -->
    <el-form inline style="float: left">
      <el-form-item>
        <el-button type="primary" size="small" icon="el-icon-plus" @click="addTagVisible = true">添加标签</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="dataSource" stripe style="width: 100%">
      <el-table-column type="index" label="序号" width="180"></el-table-column>
      <el-table-column prop="tag" label="标签名称"></el-table-column>
      <el-table-column label="操作" width="200">
        <template v-slot="scope">
          <el-button
              icon="el-icon-edit"
              size="mini"
              type="primary"
              @click="showEditTag(scope.row)"
              style="margin-right: 20px"
          >
            编辑
          </el-button>
          <el-popconfirm icon="el-icon-delete" icon-color="red" title="确定删除吗？" @confirm="deleteTagById(scope.row.id)">
            <el-button slot="reference" icon="el-icon-delete" size="mini" type="danger">删除</el-button>
          </el-popconfirm>
        </template>

      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pageNo"
        :page-sizes="[10,20,30,40,50]"
        :page-size="queryInfo.pageSize"
        :total="total"
        layout="total,sizes,prev,pager,next,jumper"
        background
        style="margin-top: 20px"
    >
    </el-pagination>

    <!-- 添加友链对话框 -->
    <el-dialog title="添加标签" width="40%" :visible.sync="addTagVisible" :close-on-click-modal="false"
               @close="addTagClose">
      <!-- 内容主体 -->
      <el-form :model="tagForm" :rules="formRules" ref="addFormRef" label-width="80px">
        <el-form-item label="名称" prop="tag">
          <el-input v-model="tagForm.tag"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部 -->
      <span slot="footer">
        <el-button @click="addTagVisible = false">取消</el-button>
        <el-button type="primary" @click="saveType">确定</el-button>
      </span>
    </el-dialog>

    <!-- 编辑友链对话框 -->
    <el-dialog title="编辑分类" width="40%" :visible.sync="editTagVisible" :close-on-click-modal="false"
               @close="editTagClose">
      <!-- 内容主体 -->
      <el-form :model="editForm" :rules="formRules" ref="addFormRef" label-width="80px">
        <el-form-item label="名称" prop="typeName">
          <el-input v-model="editForm.tag"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部 -->
      <span slot="footer">
        <el-button @click="editTagVisible = false">取消</el-button>
        <el-button type="primary" @click="editTag">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "TagList",
  data(){
    return {
      addTagVisible: false,
      editTagVisible: false,
      dataSource: [],
      queryInfo: {
        pageNo: 1,
        pageSize: 10
      },
      total: 0,
      tagForm: {
        tag: ''
      },
      editForm: {
        id: '',
        tag: ''
      },
      formRules: {
        tag: [{required: true,message: '请输入标签名称',trigger: 'blur'}]
      }
    }
  },
  created() {
    this.getTagDataSource();
  },
  methods: {
    getTagDataSource(){
      this.axios.get('/tag/getPageTag').then(res => {
        this.dataSource = res.data.data.records;
        this.total = res.data.data.total;
      })
    },
    showEditTag(record){
      this.editForm.id = record.id;
      this.editForm.tag = record.tag;
      this.editTagVisible = true;
    },
    deleteTagById(id){
      this.axios.get('/tag/delTag/'+id,{
        headers: {
          "Authorization": localStorage.getItem("token")
        }
      }).then(res => {
        if (res.data.data === "success") {
          this.$message({
            message: '操作成功!',
            center: true,
            showClose: true,
            type: 'success'
          });
          this.getTagDataSource();
        } else {
          this.$message({
            message: '操作失败!',
            center: true,
            showClose: true,
            type: 'error'
          });
        }
      })
    },
    handleSizeChange(newPageSize){
      this.queryInfo.pageSize = newPageSize;
      this.getTagDataSource();
    },
    handleCurrentChange(newPageNo){
      this.queryInfo.pageNo = newPageNo;
      this.getTagDataSource();
    },
    addTagClose(){

    },
    saveType(){
      this.axios.post('/tag/tagSave',this.tagForm,{
        headers: {
          "Authorization": localStorage.getItem("token")
        }
      }).then(res => {
        if (res.data.data === "success") {
          this.$message({
            message: '操作成功!',
            center: true,
            showClose: true,
            type: 'success'
          });
          this.addTagVisible = false;
          this.tagForm.tag = '';
          this.getTagDataSource();
        } else {
          this.$message({
            message: '操作失败，请重新输入!',
            center: true,
            showClose: true,
            type: 'error'
          });
          this.tagForm.tag = '';
        }
      })
    },
    editTagClose(){

    },
    editTag(){
      this.axios.post('/tag/tagUpdate',this.editForm,{
        headers: {
          "Authorization": localStorage.getItem("token")
        }
      }).then(res => {
        if (res.data.data === "success") {
          this.$message({
            message: '操作成功!',
            center: true,
            showClose: true,
            type: 'success'
          });
          this.editTagVisible = false;
          this.getTagDataSource();
        } else {
          this.$message({
            message: '操作失败，请重新输入!',
            center: true,
            showClose: true,
            type: 'error'
          });
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
