<template>
  <div style="margin: 20px 20px">
    <!-- 添加 -->
    <el-form inline style="float: left">
      <el-form-item>
        <el-button type="primary" size="small" icon="el-icon-plus" @click="addUserVisible = true">添加用户</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="dataSource" stripe style="width: 100%">
      <el-table-column type="index" label="序号" width="180"></el-table-column>
      <el-table-column prop="username" label="用户名"></el-table-column>
      <el-table-column prop="avatar" label="头像"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="status" label="状态">
        <template v-slot="scope">
          <div v-if="scope.row.status === 1">
            启用
          </div>
          <div v-else>
            停用
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="created" label="创建时间"></el-table-column>
      <el-table-column prop="lastLogin" label="最后登陆时间"></el-table-column>
      <el-table-column label="操作" width="200">
        <template v-slot="scope">
          <el-button
              icon="el-icon-edit"
              size="mini"
              type="primary"
              @click="showEditUser(scope.row)"
              style="margin-right: 20px"
          >
            编辑
          </el-button>
          <el-popconfirm icon="el-icon-delete" icon-color="red" title="确定删除吗？" @confirm="deleteUserById(scope.row.id)">
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
    <el-dialog title="添加用户" width="40%" :visible.sync="addUserVisible" :close-on-click-modal="false"
               @close="addUserClose">
      <!-- 内容主体 -->
      <el-form :model="userForm" :rules="formRules" ref="addFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username"></el-input>
        </el-form-item>
        <el-form-item label="头像" prop="avatar">
          <el-upload v-model="userForm.avatar"></el-upload>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-input v-model="userForm.status"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部 -->
      <span slot="footer">
        <el-button @click="addUserVisible = false">取消</el-button>
        <el-button type="primary" @click="saveUser">确定</el-button>
      </span>
    </el-dialog>

    <!-- 编辑友链对话框 -->
    <el-dialog title="编辑分类" width="40%" :visible.sync="editUserVisible" :close-on-click-modal="false"
               @close="editUserClose">
      <!-- 内容主体 -->
      <el-form :model="editForm" :rules="formRules" ref="addFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="editForm.username"></el-input>
        </el-form-item>
        <el-form-item label="头像" prop="avatar">
          <el-image v-model="editForm.avatar"></el-image>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-image v-model="editForm.email"></el-image>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-input v-model="editForm.status"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部 -->
      <span slot="footer">
        <el-button @click="editUserVisible = false">取消</el-button>
        <el-button type="primary" @click="editUser">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "UserManager",
  data(){
    return {
      addUserVisible: false,
      dataSource: [],
      queryInfo: {
        pageNo: 1,
        pageSize: 10
      },
      total: 0,
      userForm: {
        id: '',
        username: '',
        avatar: '',
        email: '',
        status: '',
        created: '',
        lastLogin: ''
      },
      formRules: {
      },
      editUserVisible: false,
      editForm: {
        id: '',
        username: '',
        avatar: '',
        email: '',
        status: '',
        created: '',
        lastLogin: ''
      },
    }
  },
  created() {
    this.getUserDataSource();
  },
  methods: {
    // 获取用户数据
    getUserDataSource(){
      this.axios.get('/user/getUser',{
        headers: {
          "Authorization": localStorage.getItem("token")
        }
      }).then(res => {
        this.dataSource = res.data.data;
      });
    },
    showEditUser(record){
      this.editUserVisible = true;


    },
    deleteUserById(){

    },
    handleSizeChange(newPageSize){
      this.queryInfo.pageSize = newPageSize;
      this.getTagDataSource();
    },
    handleCurrentChange(newPageNo){
      this.queryInfo.pageNo = newPageNo;
      this.getTagDataSource();
    },
    // 关闭添加窗口
    addUserClose(){
      this.$refs['addFormRef'].resetFields();
    },
    saveUser(){

    },
    editUserClose(){

    },
    editUser(){

    }
  }
}
</script>

<style scoped>
/deep/ .el-dialog__footer {
  padding: 10px 20px 20px;
  text-align: center;
  box-sizing: border-box;
}
</style>
