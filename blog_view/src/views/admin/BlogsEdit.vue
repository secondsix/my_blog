<template>
  <div style="width: 100%">
    <h1 style="margin: 5px auto;font-size: 24px;color: #333333">写文章</h1>
    <div>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <!-- 文章标题和文章首图 -->
        <el-row :gutter="24" class="rows">
          <el-col :span="12">
            <el-form-item label="文章标题" prop="title">
              <el-input v-model="ruleForm.title" placeholder="请输入文章标题"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24" class="rows">
          <el-col :span="12">
            <el-form-item label="文章首图" prop="firstPicture">
              <!--              <el-upload action="" placeholder="文章首图，可为空" v-model="ruleForm.firstPicture"></el-upload>-->
              <!--              <el-upload
                                class="upload-demo"
                                drag
                                action="http://jsonplaceholder.typicode.com/posts/"
                                multiple
                                v-model="ruleForm.firstPicture"
                            >
                              <i class="el-icon-upload"></i>
                              <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                              <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb，可为空</div>
                            </el-upload>-->
              <!--        :on-success="handleSuccess"       -->
              <el-upload
                  action="#"
                  list-type="picture-card"
                  :limit="1"
                  :on-preview="handlePictureCardPreview"
                  :on-change="getName"
                  :before-upload="beforeAvatarUpload"
                  :on-remove="getRemoveFile"
                  :auto-upload="false"
                  :file-list="fileList"
                  :show-file-list="fileShow"
                  ref="upload"
                  v-model="ruleForm.firstPicture"
              >
                <!--   :show-file-list="fileShow"             -->
                <!--                {{fileShow}}-->
                <i slot="default" class="el-icon-plus"></i>
              </el-upload>
              <el-dialog :visible.sync="dialogVisible">
                <img width="100%" :src="dialogImageUrl" alt="">
              </el-dialog>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24" class="rows">
          <el-form-item label="文章描述" prop="description" class="applyDialog">
            <mavon-editor
                placeholder="开始编辑..."
                @dblclick.native="autoWrite"
                v-model="ruleForm.description"
                style="margin-left: -65px;"
                :autofocus="false"
                ref="md"
                @imgAdd="imgAdd"
                @imgDel="imgDel"
            >
              <template slot="left-toolbar-after">
                <button
                    type="button"
                    @click="imgDel"
                    class="op-icon fa fa-mavon-align-left"
                    aria-hidden="true"
                    title="删除图片"
                ></button>
              </template>
            </mavon-editor>
          </el-form-item>
        </el-row>

        <el-row :gutter="24" class="rows">
          <el-form-item label="文章正文" prop="content" class="applyDialog">
            <mavon-editor
                placeholder="开始编辑..."
                v-model="ruleForm.content"
                style="margin-left: -65px;"
                :autofocus="false"
                ref="md2"
                @imgAdd="imgAdd2"
                @imgDel="imgDel2"
            >
              <template slot="left-toolbar-after">
                <button
                    type="button"
                    @click="imgDel2"
                    class="op-icon fa fa-mavon-align-left"
                    aria-hidden="true"
                    title="删除图片"
                ></button>
              </template>
            </mavon-editor>
          </el-form-item>
        </el-row>

        <el-dialog title="删除图片" :visible.sync="dialogFormVisible">
          图片地址：<el-input v-model="img_file"  autocomplete="off" style="width: 80%"></el-input>
          <div slot="footer" class="dialog-footer">
            <el-button @click="imgCancel">取 消</el-button>
            <el-button type="primary" @click="imgConfirm">确 定</el-button>
          </div>
        </el-dialog>

        <el-row :gutter="24" class="rows">
          <el-col :span="12">
            <el-form-item label="分类" prop="cate">
              <el-select
                  v-model="ruleForm.typeId"
                  :allow-create="true"
                  :filterable="true"
                  placeholder="请选择分类（输入可添加新分类）"
                  clearable
                  style="width: 100%"
              >
                <el-option v-for="type in types" :key="type.id" :label="type.typeName" :value="type.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标签" prop="tag">
              <el-select
                  v-model="ruleForm.tagId"
                  :allow-create="true"
                  :filterable="true"
                  placeholder="请选择标签（输入可添加新标签）"
                  clearable
                  style="width: 100%"
              >
                <el-option v-for="tag in tags" :key="tag.id" :label="tag.tag" :value="tag.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24" class="rows">
          <el-col :span="12">
            <el-form-item label="字数" prop="words">
              <!-- 双击显示字数 -->
              <el-input placeholder="双击可计算文章字数" v-model="ruleForm.words" type="number"
                        @dblclick.native="computeWords"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="浏览次数" prop="views">
              <el-input v-model="ruleForm.views" placeholder="请输入文章字数（可选）默认为 0" type="number"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否公开" prop="status">
              <el-tooltip :content="'Swich value:' + ruleForm.status">
                <el-switch v-model="ruleForm.status" :active-value="1" :inactive-value="0"></el-switch>
              </el-tooltip>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
          <!--          <el-button type="primary" @click="submitForm">立即创建</el-button>-->
          <el-button @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  inject: ['reload'],
  name: "BlogsEdit",
  data() {
    return {
      types: {},
      tags: {},
      ruleForm: {
        id: '',
        title: '',
        description: '',
        content: '',
        firstPicture: '',
        typeId: '',
        created: '',
        updateTime: '',
        userId: '',
        words: null,
        views: 0,
        status: '',
        tagId: ''
      },
      rules: {
        title: [
          {required: true, message: '请输入标题', trigger: 'blur'},
          {min: 2, max: 45, message: '长度在 3 到 45 个字符', trigger: 'blur'}
        ],
        description: [
          {required: true, message: '请输入摘要', trigger: 'blur'}
        ],
        content: [
          {required: true, message: '请输入内容', trigger: 'blur'}
        ],
        words: [{required: true, message: '请输入文章字数', trigger: 'change'}],
      },
      dialogImageUrl: '',
      dialogVisible: false,
      disabled: false,
      file: {},
      fileList: [],
      fileShow: null,
      uploadStatus: true,
      img_file: '',
      dialogFormVisible: false
    }
  },
  methods: {
    autoWrite() {
      //双击将正文的内容前100字自动写入文章描述中
      if (this.ruleForm.content.length < 100) {
        this.ruleForm.description = this.ruleForm.content.substring(0, this.ruleForm.content.length);
      } else {
        this.ruleForm.description = this.ruleForm.content.substring(0, 100);
      }
    },
    computeWords() {
      //双击将计算正文内容的字数
      let contents = this.ruleForm.content;
      this.ruleForm.words = contents.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, '').length;
    },
    submitForm(formName) {
      const _this = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          //id等于空字符串添加否则编辑
          if (_this.ruleForm.id === '') {
            //file.raw 文件信息等于undefined时清空首图信息
            if (_this.file.raw === undefined) {
              _this.ruleForm.firstPicture = '';
              _this.blogCreatedOrEditNoFileRow();
            } else {
              _this.blogCreatedOrEdit();
            }
          } else {
            if (_this.ruleForm.firstPicture === '') {
              if (_this.file.raw !== undefined) {
                _this.blogCreatedOrEdit();
                _this.$router.push(`/writeBlog`);
              } else {
                _this.blogCreatedOrEditNoFileRow();
                _this.$router.push(`/writeBlog`);
              }
            } else {
              //图片不为空时，先删除服务器中与数据库中名字相匹配的文件，再将新的图片上传并添加至数据库
              //判断图片是否是原来的图片
              if (_this.file.raw !== undefined) {
                //不是原来的图片就删除服务器中的图片和数据库中的地址
                let fileName = new FormData;
                fileName.append('fileName', _this.ruleForm.firstPicture);
                this.axios.post('/databaesDeleteFile', fileName).then(res => {
                  if (res.data.data === 'success') {
                    _this.blogCreatedOrEdit();
                    _this.$router.push(`/writeBlog`);
                  } else {

                  }

                });
              } else {
                //数据库中查询出来有图片，但页面上已经删除，此时要删除服务器上的图片及数据库中的地址
                if (_this.ruleForm.firstPicture !== '') {
                  if (_this.uploadStatus) {
                    _this.blogCreatedOrEditNoFileRow();
                  } else {
                    let fileName = new FormData;
                    fileName.append('fileName', _this.ruleForm.firstPicture);
                    this.axios.post('/databaesDeleteFile', fileName).then(res => {
                      if (res.data.data === 'success') {
                        _this.ruleForm.firstPicture = '';
                        _this.blogCreatedOrEditNoFileRow();
                        _this.$router.push(`/writeBlog`);
                        _this.uploadStatus = true;
                      } else {

                      }
                    });
                  }

                } else {
                  _this.blogCreatedOrEditNoFileRow();
                  _this.$router.push(`/writeBlog`);
                }

              }


            }
          }
        } else {
          console.log("submit error!");
          return false;
        }
      })
    },
    blogCreatedOrEditNoFileRow() {
      const _this = this;
      this.axios.post("/blog/edit", _this.ruleForm, {
        headers: {
          "Authorization": localStorage.getItem("token")
        }
      }).then(res => {
        if (res.data.data === "操作成功!") {
          _this.$message({
            message: '操作成功!',
            center: true,
            showClose: true,
            type: 'success'
          });
          _this.reload();
        } else {
          this.$message({
            message: '操作失败!',
            center: true,
            showClose: true,
            type: 'error'
          });
        }
      });
    },
    blogCreatedOrEdit() {
      const _this = this;
      let fd = new FormData;
      fd.append('file', this.file.raw);
      // console.log(fd.get('file'));

      //先删除数据库中保存的文件名与storage存储服务器中的文件相同的文件

      this.axios.post('/upload', fd, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }).then(res => {
        this.ruleForm.firstPicture = res.data.data;

        this.axios.post("/blog/edit", _this.ruleForm, {
          headers: {
            "Authorization": localStorage.getItem("token")
          }
        }).then(res => {
          if (res.data.data === "操作成功!") {
            _this.$message({
              message: '操作成功!',
              center: true,
              showClose: true,
              type: 'success'
            });
            // _this.$router.push(`/writeBlog`);
            _this.reload();
          } else {
            this.$message({
              message: '操作失败!',
              center: true,
              showClose: true,
              type: 'error'
            });
          }
        })
      });
    },
    resetForm(formName) {
      //重置博客内容
      this.$refs[formName].resetFields();
    },
    getTag() {
      const _this = this;
      this.axios.get("/tag/getTagAll").then(res => {
        // console.log(res.data);
        _this.tags = res.data.data;
      })
    },
    getType() {
      const _this = this;
      this.axios.get("/type/getTypeAll").then(res => {
        _this.types = res.data.data;
      })
    },
    getBlogById() {
      //通过id获取博客
      const _this = this;
      let blogId = this.$route.params.blogId;
      if (blogId !== undefined) {
        this.axios.get('/blog/' + blogId).then(res => {
          const blog = res.data.data
          _this.ruleForm.id = blog.id
          _this.ruleForm.title = blog.title
          _this.ruleForm.firstPicture = blog.firstPicture
          _this.ruleForm.description = blog.description
          _this.ruleForm.content = blog.content
          _this.ruleForm.words = blog.words
          _this.ruleForm.views = blog.views
          _this.ruleForm.typeId = blog.typeId
          _this.ruleForm.tagId = blog.tagId
          _this.ruleForm.status = blog.status
          _this.ruleForm.userId = blog.userId
          _this.ruleForm.createTime = blog.createTime
          _this.ruleForm.updateTime = blog.updateTime

          if (blog.firstPicture !== null && blog.firstPicture !== '') {
            const data = {
              id: blog.id,
              url: blog.firstPicture,
            };

            _this.fileList.push(data);
            _this.fileShow = true;
          } else {

          }
        });
      }
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    getName(file) {
      // console.log(file);
      this.file = file;
      // this.beforeAvatarUpload(file);
      // console.log("------------------------")
      let show = this.beforeAvatarUpload(file);
      // console.log(file);
      if (show) {
        // 判断show是否为true
        this.fileShow = true;
      } else {
        this.fileShow = false;
        this.getRemoveFile();
      }
      // console.log(this.fileShow);
    },
    upload(file) {
      let fd = new FormData;
      fd.append('file', file.raw);
      this.axios.post('/upload', fd, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }).then(res => {
        this.ruleForm.firstPicture = res.data.data;
      });
    },
    beforeAvatarUpload(file) {
      const isJPG = file.raw.type === 'image/jpeg' || file.raw.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG及PNG 格式!');
        // console.log(file);
        // console.log(this.fileShow);
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    },
    getRemoveFile(file, fileList) {
      // console.log(this.file.raw);
      // console.log(this.ruleForm.firstPicture);
      this.$refs.upload.clearFiles();//调用element官方的方法
      // this.ruleForm.firstPicture = '';
      this.file = {};
      this.uploadStatus = false;
      // console.log(file);
      // console.log(fileList);
    },
    imgAdd(pos,file){
      let imgData = new FormData;
      imgData.append('file',file);
      this.axios.post('/upload',imgData).then(res => {
        this.$refs.md.$img2Url(pos,res.data.data);
      });
    },
    imgAdd2(pos,file){
      let imgData = new FormData;
      imgData.append('file',file);
      this.axios.post('/upload',imgData).then(res => {
        const resData = res.data.data;
        this.$refs.md2.$img2Url(pos,resData);

      });
    },
    imgDel(){
      this.dialogFormVisible = true;
    },
    imgDel2(){
      this.dialogFormVisible = true;
    },
    imgCancel(){
      this.dialogFormVisible = false;
    },
    imgConfirm(){
      this.dialogFormVisible = false;
      let fd = new FormData;
      fd.append('fileName',this.img_file);
      this.axios.post('/databaesDeleteFile',fd).then(res => {
        if (res.data.data === 'success') {
          this.$message({
            message: '操作成功!',
            center: true,
            showClose: true,
            type: 'success'
          });
          this.img_file = '';
        } else {
          this.$message({
            message: '操作失败!',
            center: true,
            showClose: true,
            type: 'error'
          });
        }
      });
    }

  },
  created() {
    this.getTag();
    this.getType();
    this.getBlogById();
  },

}
</script>

<style scoped>
/deep/ .applyDialog .el-form-item__label {
  margin-left: -90%;
  /*加下面两条就行*/
  float: none;
  /*取消浮动*/
  word-break: break-word;
  /*支持单词截断换行*/

}

.rows {
  margin-right: 0 !important;
  margin-left: 0 !important;
}

/deep/ .el-form-item__content {
  padding-right: 20px;
}

</style>
