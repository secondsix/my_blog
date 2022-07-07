<template>
  <div style="margin: 20px 20px">
    <el-table :data="dataSource" stripe style="width: 100%">
      <el-table-column type="index" label="序号" width="180"></el-table-column>
      <el-table-column prop="title" label="标题" show-overflow-tooltip></el-table-column>
      <el-table-column prop="typeName" label="分类" width="180"></el-table-column>
      <el-table-column prop="tagName" label="标签" width="180"></el-table-column>
      <el-table-column label="创建时间" width="170">
        <template v-slot="scope">{{ scope.row.created }}</template>
      </el-table-column>
      <el-table-column label="最近更新" width="170">
        <template v-slot="scope">{{ scope.row.updateTime }}</template>
      </el-table-column>
      <el-table-column label="可见性" width="100">
        <template v-slot="scope">
          <el-switch v-model="scope.row.status" :active-value="1" :inactive-value="0"
                     @change="blogPublishedChanged(scope.row.id)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="username" label="用户名" width="170"></el-table-column>
      <el-table-column label="操作" width="200">
        <template v-slot="scope">
          <el-button
              icon="el-icon-edit"
              size="mini"
              type="primary"
              @click="blogEditById(scope.row.id)"
              style="margin-right: 20px"
          >
            编辑
          </el-button>
          <el-popconfirm icon="el-icon-delete" icon-color="red" title="确定删除吗?"
                         @confirm="deleteBlogById(scope.row.id,scope.row)">
            <el-button slot="reference" icon="el-icon-delete" size="mini" type="danger">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!--分页-->
    <div class="home-page" style="margin-top: 20px">
      <!--分页-->
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                     :current-page="queryInfo.pageNo"
                     :page-sizes="[10, 20, 30, 50]" :page-size="queryInfo.pageSize" :total="total"
                     layout="total, sizes, prev, pager, next, jumper" background>
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  inject: ['reload'],
  name: "BlogList",
  data() {
    return {
      dataSource: [],
      queryInfo: {
        title: '',
        categoryId: null,
        pageNo: 1,
        pageSize: 10
      },
      total: 0,
      types: [],
      tags: [],
      user: []
    }
  },
  created() {
    this.getBlogDataSource();
  },
  methods: {
    getBlogDataSource() {
      const _this = this;
      this.axios.get('/blog/getData?pageNo=' + this.queryInfo.pageNo + "&pageSize=" + this.queryInfo.pageSize, {
        headers: {
          "Authorization": localStorage.getItem("token")
        }
      }).then(res => {
        // console.log(res.data.data);
        _this.dataSource = res.data.data.records;
        _this.total = res.data.data.total;

      })
    },
    handleSizeChange(newPageSize) {
      this.queryInfo.pageSize = newPageSize;
      this.getBlogDataSource();

    },
    handleCurrentChange(newPage) {
      this.queryInfo.pageNo = newPage;
      this.getBlogDataSource();
    },
    blogPublishedChanged(id) {
      const _this = this;
      this.axios.get('/blog/show?id=' + id, {
        headers: {
          "Authorization": localStorage.getItem("token")
        }
      }).then(res => {
        _this.$alert('操作成功', '提示', {
          confirmButtonText: '确定',
          callback: action => {
            // _this.page(_this.currentPage);
          }
        })
      })
    },
    blogEditById(id) {
      this.$router.push(`/blog/edit/${id}`);
    },
    deleteImg(id){
      let fd = new FormData;
      fd.append('id',id);
      this.axios.post('/blog/deleteDImg',fd).then(res => {

      });
    },
    deleteBlogById(id, row) {
      //判断首图地址是否为空，为空先删除服务器中的图片，再删除数据库中的数据
      if (row.firstPicture !== '') {
        let fileName = new FormData;
        fileName.append('fileName',row.firstPicture);
        this.axios.post('/databaesDeleteFile',fileName).then(res => {
          if (res.data.data === 'success'){
            this.deleteImg(id);
            this.deleteByIdExist(id);
          }else {

          }

        });
      } else {
        this.deleteImg(id);
        this.deleteByIdExist(id);
      }
    },
    deleteByIdExist(id){
      const _this = this;
      this.axios.post('/blog/blogdel/' + id, null, {
        headers: {
          "Authorization": localStorage.getItem("token")
        }
      }).then(res => {
        // console.log(res);
        if (res.data.data === '文章删除成功') {
          _this.$message({
            message: '操作成功!',
            center: true,
            showClose: true,
            type: 'success'
          });
          _this.reload();
        } else {
          _this.$message({
            message: '操作失败!',
            center: true,
            showClose: true,
            type: 'error'
          });
        }
      });
    }
  },

}
</script>

<style scoped>

</style>
