<template>
  <!-- 网站底部 -->
  <div class="footer">
    <el-row :gutter="24" style="height: 20px;margin-top: 25px;font-size: 12px;line-height: 30px">
      <el-col :span="24">
        Copyright&nbsp;&copy;&nbsp;2021-2022&nbsp;Arthur's&nbsp;Blog.&nbsp;All&nbsp;rights&nbsp;reserved.&nbsp;
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "Footer"
}
</script>

<style scoped>
.footer-title {
  font-size: 13px;
  font-weight: bold;
  margin-bottom: 5px;
}

.footer-content {
  font-size: 13px;
}

.footer {
  margin: 0 auto;
  text-align: center;
  min-height: 30px;
  max-width: 100%;
  padding: 0 15px;
}
</style>
