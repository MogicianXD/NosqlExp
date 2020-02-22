<template>
  <div>
    <el-tabs tab-position="left">
      <el-tab-pane v-for="(v,i) in meta" :key="i" :label="v.label">
        <el-table :data="v.tableData.slice((v.currentPage-1)*10,v.currentPage*10)">
          <el-table-column type="index"/>
          <el-table-column
            width="200px"
            v-for="(c,j) in v.columns"
            :key="j"
            :prop="c.field"
            :label="c.title"
          ></el-table-column>
        </el-table>        
        <div style="text-align: center;margin-top: 24px;">
          <el-pagination
            :current-page.sync="v.currentPage"
            layout="total, prev, pager, next, jumper"
            :total="v.totalItems"
          ></el-pagination>
        </div>
        <echarts ref="chart" v-if="v.showPie" style="width:100%; height: 300px; margin-top: 50px;"
          titleText="成绩分布"
          tooltipFormatter="人数"
          :opinion="options"
          :opinionData="distribution"
        ></echarts>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
// import echarts from 'echarts'
import echarts from './Echarts.vue'

export default {
  name: 'Diagram',
  created() {
    var _this = this;
    for(let i = 0; i < 9; i++){
      this.$axios.analysis(i).then(
        response => {
          // 成功回调
          var res = response.data;
          console.log(res);
          if (res.data.res == true) {
            _this.meta[i].tableData = res.data.analysis;
            _this.meta[i].totalItems = res.data.analysis.length;
            if(i == 4){
              let j = 0;
              for(var key in res.data.analysis[0]){
                _this.distribution[j] = { name: key, value: res.data.analysis[0][key]};
                j++;
              }
              _this.meta[4].showPie = true;
              // _this.$refs.chart.refreshData(_this.distribution);
            }
            // this.$message({ message: "查询成功", type: "success" });
          } else {
            console.log("null");
            // this.$message("没有查询到相关结果");
            _this.meta[i].tableData = [];
            _this.meta[i].totalItems = 0;
          }
        },
        response => {
          // 错误回调
          var result = response;
          console.log(result);
          this.$message.error("查询失败，请查看网络是否正常");
        }
      );
    }
  },  
  components: {
      'echarts': echarts, 
  },
  data() {
    return {
      options: ["excellent", "good", "pass", "fail"], 
      distribution: [{name: "excellent", value: 0}, 
                    {name: "good", value: 0}, 
                    {name: "pass", value: 0}, 
                    {name: "fail", value: 0}, ], 
      meta: [
        {
          label: "有学生选修的所有课程名称",
          columns: [{ field: "name", title: "课程名" }],
          tableData: [], 
          currentPage: 1,
          totalItems: 0,
        },
        {
          label: "平均成绩排名前10的学生",
          columns: [
            { field: "sid", title: "学号" },
            { field: "name", title: "姓名" },
            { field: "score", title: "平均成绩" }
          ],
          tableData: [],
          currentPage: 1,
          totalItems: 0,
        },
        {
          label: "选课数目排名前10的学生",
          columns: [
            { field: "sid", title: "学号" },
            { field: "name", title: "姓名" },
            { field: "count", title: "选修课程数" }
          ],
          tableData: [],
          currentPage: 1,
          totalItems: 0,
        },
        {
          label: "每位同学的最高成绩以及最高成绩对应的课程名",
          columns: [
            { field: "sid", title: "学号" },
            { field: "name", title: "姓名" },
            { field: "score", title: "最高成绩" },
            { field: "cname", title: "课程名" }
          ],
          tableData: [],
          currentPage: 1,
          totalItems: 0,
        },
        {
          label: "每位同学的成绩分布",
          columns: [
            { field: "excellent", title: "优秀" },
            { field: "good", title: "良好" },
            { field: "pass", title: "合格" },
            { field: "fail", title: "不合格" }
          ],
          tableData: [],
          currentPage: 1,
          totalItems: 0,
      showPie: false, 
        },
        {
          label: "每门课程的选修人数和平均成绩",
          columns: [
            { field: "name", title: "课程名" },
            { field: "count", title: "选修人数" },
            { field: "score", title: "平均成绩" }
          ],
          tableData: [],
          currentPage: 1,
          totalItems: 0,
        },
        {
          label: "每门课程最高成绩以及最高成绩对应的学生姓名",
          columns: [
            { field: "cname", title: "课程名" },
            { field: "name", title: "姓名" },
            { field: "score", title: "最高成绩" }
          ],
          tableData: [],
          currentPage: 1,
          totalItems: 0,
        },
        {
          label: "平均成绩排名前10的课程",
          columns: [
            { field: "name", title: "课程名" },
            { field: "score", title: "平均成绩" }
          ],
          tableData: [],
          currentPage: 1,
          totalItems: 0,
        },
        {
          label: "选课人数排名前10的课程",
          columns: [
            { field: "name", title: "课程名" },
            { field: "count", title: "选修人数" }
          ],
          tableData: [],
          currentPage: 1,
          totalItems: 0,
        }
      ], 
    };
  },
  methods: {
  }
};
</script>

<style>
.el-table {
  margin-left: 80px;
}
</style>
