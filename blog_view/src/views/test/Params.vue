<template>
  <div>
    <!-- list-type 图片的显示类型 -->
    <!-- action 图片要上次服务器的地址 -->
    <!-- on-preview 点击文件列表中已上传的文件时的触发的钩子函数（点击显示对应的预览图片） -->
    <!-- on-remove 点击删除图片的时候触发的钩子函数 -->
    <!-- headers 上传可能需要携带的请求头部参数（不需要可以不加） -->
    <el-upload
        action="#"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :headers="headerObj"
        :on-success="handleSuccess"
        list-type="picture-card"
        :auto-upload="false"
    >
      <el-button size="small" type="primary">点击上传</el-button>
    </el-upload>

    <!-- 图片预览弹框 -->
    <el-dialog title="图片预览" :visible.sync="previewDialogVisible" width="40%"  class="dialog-picture" >
      <img :src="picPreviewPath" alt="" class="previewImg" />
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Params",
  data() {
    return {
      // 图片上传地址
      uploadURL: "",

      // 图片上传组件的请求对象
      headerObj: {
        Authorization: localStorage.getItem("token"),
      },

      //定义一个数组来存放路径
      pictureList: [],

      //预览的图片路径
      picPreviewPath: null,

      //预览图片弹框的状态(默认关闭)
      previewDialogVisible: false,
    };
  },
  methods: {
    // 监听图片上传成功事件
    handleSuccess(response) {
      //返回的对象
      // console.log(response)

      //这是本地的图片路径
      // console.log(response.data.url)

      //得到一个图片信息对象 临时路径
      const picInfo = { pic: response.data.tmp_path };

      // 将图片信息对象，push到pictureList数组中
      this.pictureList.push(picInfo);
    },
    //点击图片，利用弹框显示出预览图
    handlePreview(file) {
      console.log("你点击了图片");
      //console.log(file)
      //获取到本地图片路径
      this.picPreviewPath = file.response.data.url;

      //显示出对应的预览图片
      this.previewDialogVisible = true;
    },
    handleRemove(file) {
      console.log("你删除了图片");

      // 1.获取将要删除图片的临时路径
      const filePath = file.response.data.tmp_path;

      // 2.从pictureList数组中，找到图片对应的索引值
      const i = this.pictureList.findIndex((x) => x.pic === filePath);

      // 3.调用splice方法，移除图片信息
      this.pictureList.splice(i, 1);

      console.log("删除后的" + this.pictureList);
    },
  },
};
</script>

<style scoped>
.dialog-picture {
  text-align: center;
}
.previewImg {
  width: 56%;
}
</style>
