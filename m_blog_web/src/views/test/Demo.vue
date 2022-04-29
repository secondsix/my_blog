<template>
  <div class="Demo">
    {{msg}}
    <el-button type="primary">主要按钮</el-button>
  </div>
</template>
<script>
export default{
  name: "DemoTest",
  data() {
    return {
      msg: "测试"
    };
  }
}
</script>
