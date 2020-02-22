<template>
  <div>
    <el-row>
      <el-col style="width: 140px">
        <el-menu
          :default-active="$router.path"
          class="el-menu-vertical-demo"
          router
        >
          <el-menu-item index="/elect/mine">
            <i class="el-icon-finished"></i>
            <span slot="title">我的选课</span>
          </el-menu-item>
          <el-menu-item index="/elect/all">
            <i class="el-icon-search"></i>
            <span slot="title">全部课程</span>
          </el-menu-item>
        </el-menu>
      </el-col>      
        <div id="toelect">
           <!-- 存放跳转后的页面 --> 
          <router-view></router-view>
       </div>
    </el-row>
  </div>
</template>

<script>

export default {
  data() {
    return {
    }
  },
  methods: {
  },
};
</script>

