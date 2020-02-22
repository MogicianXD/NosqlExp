<template>
  <div :gutter="20">
      <el-col :span="1" style="margin-left: 20px; margin-top: 5px;">
        <el-button primary icon="el-icon-refresh-left" circle @click="refresh"></el-button>
      </el-col>
      <el-col :span="19">
        <el-table
          ref="table"
          :data="tableData.slice((currentPage-1)*pagesize,currentPage*pagesize)"
          style="width: 100%"
        >
          <el-table-column
            v-for="(v,i) in columns"
            :key="i"
            :prop="v.field"
            :label="v.title"
            :width="v.width"
          >
          </el-table-column>
          <el-table-column label="操作" width="90">
            <template slot-scope="scope">
              <el-button
                size="mini"
                @click="handleElect(scope.$index, scope.row)"
                type="primary"
                style="opacity: 0.9"
              >选课</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="text-align: center;margin-top: 24px;">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page.sync="currentPage"
            :page-size="pagesize"
            layout="total, prev, pager, next, jumper"
            :total="totalItems"
          ></el-pagination>
        </div>
      </el-col>
  </div>
</template>

<script>

export default {
  created(){
    if(!this.$store.state.login){ 
      this.$store.dispatch('change');
   } else {
     this.refresh();
   }
  },
  data() {
    var checkID = (rule, value, callback) => {
      if (!value) return callback(new Error("如要添加数据，ID不能为空"));
      else if (String(value).length != 6)
        return callback(new Error("如要添加数据，ID应为6位"));
      else callback();
    };
    return {
      columns: [
        { field: "cid", title: "课程号" },
        { field: "name", title: "课程名" },
        { field: "credit", title: "学分" },
        { field: "fcid", title: "先导课程号" }, 
        { field: "fcname", title: "先导课程名" }, 
      ],
      ploading: false,
      qloading: false,
      currentPage: 1,
      pagesize: 10,
      totalItems: 0,
      rules: {
        cid: [{ validator: checkID, trigger: "change" }]
      },
      tableData: []
    };
  },
  methods: {
    refresh() {
      console.log(this.$store.state.id);
      let _this = this;
      _this.qloading = true;
      this.$axios.queryAll().then(
        response => {
          // 成功回调
          var res = response.data;
          console.log(res);
          _this.qloading = false;
          if (res.data.res == true) {
            _this.totalItems = res.data.course.length;
            _this.tableData = res.data.course;
            this.$message({ message: "查询成功", type: "success" });
          } else {
            console.log("null");
            this.$message("没有查询到相关结果");
            _this.tableData = [];
            _this.totalItems = 0;
          }
        },
        response => {
          // 错误回调
          var result = response;
          this.$message.error("查询失败，请查看网络是否正常");
          _this.qloading = false;
          _this.tableData = [];
          _this.totalItems = 0;
        }
      );
      console.log("submit!");
    },

    handleCurrentChange(currentPage) {
      this.currentPage = currentPage;
    }, //组件自带监控当前页码

    handleSizeChange(curSize) {
      this.pagesize = curSize;
    },

    handleElect(index, row) {
      this.$axios.elect(this.$store.state.id, row.cid).then(
        response => {
          // 成功回调
          var res = response.data;
          console.log(res);
          if (res.data.res == true) {
            this.$message({ message: "选课成功", type: "success" });
          } else {
            this.$message("选课失败，刷新重试？");
          }
        },
        response => {
          // 错误回调
          var result = response;
          this.$message.error("查询失败，请查看网络是否正常");
        }
      );
    },
  }
};
</script>

