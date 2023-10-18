<template>
  <div style="margin: 20px 20px">

    <!-- 添加 -->
    <el-form inline style="float: left">
      <el-form-item>
        <el-button type="primary" size="small" icon="el-icon-plus" @click="addDialogVisible = true">添加分类</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="dataSource" stripe style="width: 100%">
      <el-table-column type="index" label="序号" width="180"></el-table-column>
      <el-table-column prop="typeName" label="分类名称"></el-table-column>
      <el-table-column label="操作" width="200">
        <template v-slot="scope">
          <el-button
              icon="el-icon-edit"
              size="mini"
              type="primary"
              @click="showEditDialog(scope.row)"
              style="margin-right: 20px"
          >
            编辑
          </el-button>
          <el-popconfirm icon="el-icon-delete" icon-color="red" title="确定删除吗？" @confirm="deleteTypeById(scope.row.id)">
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
    >
    </el-pagination>

    <!-- 添加友链对话框 -->
    <el-dialog title="添加分类" width="40%" :visible.sync="addDialogVisible" :close-on-click-modal="false"
               @close="addDialogClose">
      <!-- 内容主体 -->
      <el-form :model="addForm" :rules="formRules" ref="addFormRef" label-width="80px">
        <el-form-item label="名称" prop="typeName">
          <el-input v-model="addForm.typeName"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部 -->
      <span slot="footer">
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveType">确定</el-button>
      </span>
    </el-dialog>

    <!-- 编辑友链对话框 -->
    <el-dialog title="编辑分类" width="40%" :visible.sync="editDialogVisible" :close-on-click-modal="false"
               @close="editDialogClose">
      <!-- 内容主体 -->
      <el-form :model="editForm" :rules="formRules" ref="addFormRef" label-width="80px">
        <el-form-item label="名称" prop="typeName">
          <el-input v-model="editForm.typeName"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部 -->
      <span slot="footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="editType">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "CategoryList",
  data() {
    return {
      dataSource: [],
      addDialogVisible: false,
      editDialogVisible: false,
      queryInfo: {
        pageNo: 1,
        pageSize: 10
      },
      total: 0,
      addForm: {
        typeName: ''
      },
      editForm: {
        id: '',
        typeName: ''
      },
      formRules: {
        typeName: [{required: true, message: '请输入分类名称', trigger: 'blur'}]
      }
    }
  },
  created() {
    this.getDataSource();
  },
  methods: {
    getDataSource() {
      this.axios.get('/type/getPageAll?pageNo=' + this.queryInfo.pageNo + "&pageSize=" + this.queryInfo.pageSize).then(res => {
        // console.log(res);
        this.dataSource = res.data.data.records;
        this.total = res.data.data.total;
      })
    },
    showEditDialog(row) {
      this.editDialogVisible = true;
      this.editForm.id = row.id;
      this.editForm.typeName = row.typeName;
    },
    deleteTypeById(id) {
      // console.log(id);
      this.axios.get('/type/del/' + id, {
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
              this.getDataSource();
            } else {
              this.$message({
                message: '操作失败，请重新输入!',
                center: true,
                showClose: true,
                type: 'error'
              });
            }
      })
    },
    handleSizeChange(newPageSize) {
      this.queryInfo.pageSize = newPageSize;
      this.getDataSource();
    },
    handleCurrentChange(newPage) {
      this.queryInfo.pageNo = newPage;
      this.getDataSource();
    },
    addDialogClose() {
      this.$refs['addFormRef'].resetFields();
    },
    saveType() {
      this.axios.post('/type/save', this.addForm, {
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
          this.addDialogVisible = false;
          this.getDataSource();
        } else {
          this.$message({
            message: '操作失败，请重新输入!',
            center: true,
            showClose: true,
            type: 'error'
          });
        }
      })
      this.$refs['addFormRef'].resetFields();
    },
    editDialogClose() {

    },
    editType() {
      this.axios.post('/type/update', this.editForm, {
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
          this.editDialogVisible = false;
          this.getDataSource();
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
