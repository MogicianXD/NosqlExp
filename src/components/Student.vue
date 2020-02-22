<template>
  <div :gutter="20">
    <el-row>
      <el-col :span="6" style="margin-top: 16px; padding-left: 12px; padding-right: 12px">
        <el-form
          ref="form"
          :model="form"
          status-icon
          :rules="rules"
          label-width="80px"
          style="width: 100%;"
        >
          <el-form-item label="学号" prop="sid">
            <el-input clearable v-model.number="form.sid"></el-input>
          </el-form-item>
          <el-form-item label="姓名">
            <el-input clearable v-model="form.name"></el-input>
          </el-form-item>
          <el-form-item label="性别">
            <el-select v-model="form.sex" placeholder="可选择性别" style="width: 100%;">
              <el-option label="男" value="男"></el-option>
              <el-option label="女" value="女"></el-option>
              <el-option label="无筛选" value=null></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="年龄">
            <el-col :span="12">
              <el-select placeholder="可选比较符" v-model="form.cmp" style="width: 90%;">
                <el-option label="无比较" value></el-option>
                <el-option label="=" value="eq"></el-option>
                <el-option label=">" value="gt"></el-option>
                <el-option label="<" value="lt"></el-option>
                <el-option label="≤" value="le"></el-option>
                <el-option label="≥" value="ge"></el-option>
                <el-option label="≠" value="ne"></el-option>
              </el-select>
            </el-col>
            <el-col :span="12">
              <el-form-item placeholder="输入年龄筛选" style="width: 100%;">
                <el-input clearable v-model.number="form.age"></el-input>
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-form-item label="院系">
            <el-input clearable v-model="form.dname"></el-input>
          </el-form-item>
          <el-form-item label="班级">
            <el-input clearable v-model="form.class"></el-input>
          </el-form-item>
          <el-form-item>
            <el-col :span="12">
              <el-button
                id="query_btn"
                :loading="ploading"
                type="primary"
                @click="post('form')"
                style="width: 75%;"
              >插入</el-button>
            </el-col>
            <el-col :span="12">
              <el-button
                type="primary"
                :loading="qloading"
                @click="get"
                style="float: right; width: 75%;"
              >查询</el-button>
            </el-col>
          </el-form-item>
        </el-form>
        <el-row type="flex" justify="start" style="margin-left: 80px;">
          <el-tooltip class="item" effect="dark" content="上传excel" placement="left-start">
            <el-upload
              class="upload-demo"
              action="/NosqlExp/upload"
              :data="upload_data"
              :on-success="onSuccess"
              multiple
              :file-list="fileList"
            >
              <el-button>
                <i class="el-icon-upload el-icon--right"></i>
              </el-button>
            </el-upload>
          </el-tooltip>
        </el-row>
      </el-col>
      <el-col :span="15" push="1">
        <el-table
          ref="table"
          :data="tableData.slice((currentPage-1)*pagesize,currentPage*pagesize)"
          style="width: 100%"
          highlight-current-row
          @row-dblclick="handleRowClick"
          @current-change="handleCurrentChange"
        >
          <el-table-column v-for="(v,i) in columns" :key='i' :prop="v.field" :label="v.title" :width="v.width">
              <template slot-scope="scope">
                  <span v-if="scope.row.origin">{{scope.row[v.field]}}</span>
                  <el-input v-else placeholder="" v-model="scope.row[v.field]" 
                    @change="handleEdit(scope.$index, scope.row)">
                  </el-input>
              </template>
          </el-table-column>
          <el-table-column label="操作"  width="90">
            <template slot-scope="scope">
              <el-button v-if="scope.row.origin" size="mini" @click="handleModify(scope.$index, scope.row)" type="primary" style="opacity: 0.9" icon="el-icon-edit" circle></el-button>
              <el-button v-else size="mini" @click="handleCheck(scope.$index, scope.row)" type="success" style="opacity: 0.9" icon="el-icon-check" circle></el-button>
              <el-button v-if="scope.row.origin" size="mini" @click="handleDelete(scope.$index, scope.row)" type="danger" style="opacity: 0.9" icon="el-icon-delete" circle></el-button>
              <el-button v-else size="mini" @click="handleCancel(scope.$index, scope.row)" type="info" 
              style="opacity: 0.9" icon="el-icon-close" circle></el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="text-align: center;margin-top: 24px;">
          <el-pagination
            :hide-on-single-page="value"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page.sync="currentPage"
            layout="total, prev, pager, next, jumper"
            :total="totalItems"
          ></el-pagination>
        </div>
      </el-col>
      <el-col></el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  data() {
    var checkID = (rule, value, callback) => {
      if (!value) return callback(new Error("如要修改数据，ID不能为空"));
      else if (String(value).length != 12)
        return callback(new Error("如要修改数据，ID应为12位"));
      else callback();
    };
    return {
      oldRowJson: null,     
      columns: [
          { field: "sid", title: "学号", width: 150 },
          { field: "name", title: "姓名", width: 105 },
          { field: "sex", title: "性别", width: 78 },
          { field: "age", title: "年龄", width: 78 },
          { field: "birthday", title: "出生日期", width: 125 }, 
          { field: "dname", title: "院系名称", width: 220 },          
          { field: "grade", title: "班级", width: 100 },
      ],
      ploading: false,
      qloading: false,
      currentPage: 1,
      pagesize: 10,
      totalItems: 0,
      upload_data: {
        tablename: "student",
        colfamily: "info"
      },
      form: {
        sid: null,
        name: null,
        sex: null,
        age: null,
        cmp: null,
        dname: null,
        class: null
      },
      rules: {
        sid: [{ validator: checkID, trigger: "change" }]
      },
      tableData: [],
      fileList: []
    };
  },
  methods: {
    get() {
      let _this = this;
      _this.qloading = true;
      // console.log(_this.form);
      this.$axios.getStudent(_this.form).then(
        response => {
          // 成功回调
          var res = response.data;
          console.log(res);
          _this.qloading = false;
          if (res.data.res == true) {
            _this.totalItems = res.data.student.length;
            res.data.student.map(i => {
                        i.origin=true;
                        return i;
                    });
            _this.tableData = res.data.student;
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

    post(formName) {
      let _this = this;
      _this.$refs[formName].validate(valid => {
        if (!valid) return false;
        else {
          _this.ploading = true;
          console.log("form", _this.form);
          _this.$axios.postStudent(_this.form).then(
            response => {
              // 成功回调
              _this.ploading = false;
              var res = response.data;
              console.log(res);
              if (res.data.res == true) {
                this.$message({ message: "添加成功", type: "success" });
                _this.get();
              } else {
                this.$message(
                  "添加失败，请检查表单，如除了id，必须有一个属性不为空"
                );
              }
            },
            response => {
              // 错误回调
              var result = response;
              this.$message.error("查询失败，请查看网络是否正常");

              _this.ploading = false;
            }
          );
        }
      });
    },

    onSuccess(response, file, fileList) {
      console.log(response);
      if (response.data.res == true) {
        this.$message({ message: response.msg, type: "success" });
      } else {
        this.$message.error(response.msg);
      }
    },
    
    handleCurrentChange(currentPage) {
      this.currentPage = currentPage;
    }, //组件自带监控当前页码

    handleSizeChange(curSize) {
      this.pagesize = curSize;
    },

    handleEdit(index, row) {
      console.log(index, row);
    },

    handleDelete(index, row) {
      let data = { tablename: "student", id: row.sid }
      console.log(index, row, data);
      this.$axios.delete(data).then(
        response => {
          // 成功回调
          var res = response.data;
          console.log(res);
          if (res.data.res == true) {
            this.$message({ message: "删除成功", type: "success" });
            // this.get();
            this.tableData.splice(index, 1);
          } else {
            this.$message(
              "删除失败，刷新重试？"
            );
          }
        },
        response => {
          // 错误回调
          var result = response;
          this.$message.error("查询失败，请查看网络是否正常");
        }
      );
    },

    handleRowClick(row, event, column) {
      if(row.origin) {
        this.oldRowJson = JSON.stringify(row);
        this.$set(row, "origin", false);
      }
      console.log(row, event, column, event.currentTarget, JSON.stringify(row));
    },

    handleCurrentChange(currentRow, oldCurrentRow) {
      if(oldCurrentRow && !oldCurrentRow.origin) {
        let data = JSON.parse(this.oldRowJson);
        for (let k in data) oldCurrentRow[k] = data[k];
        this.$set(oldCurrentRow, "origin", true);
      }
      console.log('current change')
    },

    handleCancel(index, row) {
      console.log(index, row, JSON.parse(this.oldRowJson));
      let data = JSON.parse(this.oldRowJson);
      for (let k in data) row[k] = data[k];
      // this.$set(row, "origin", true);
      console.log(index, row, JSON.parse(this.oldRowJson));
    },

    handleModify(index, row) {
      this.oldRowJson = JSON.stringify(row);
      this.$set(row, "origin", false);
      console.log(index, row);
    },

    handleCheck(index, row) {
      this.$set(row, "origin", true);
      console.log(index, row);
      if(row.sid == null)
      {
        this.$message.error("id不能置空");
        return;
      }
      let data = row;
      data.update = true;
      let oldData = JSON.parse(this.oldRowJson);
      if(data.sid != oldData.sid)
      {
        data.old_sid = oldData.sid;
      }
      this.$axios.postStudent(data).then(
        response => {
          // 成功回调
          var res = response.data;
          console.log(res);
          if (res.data.res == true) {
            this.$message({ message: "更新成功", type: "success" });
            // this.get();
          } else {
            this.$message(
              "更新失败，请检查表单，如除了id，必须有一个属性不为空"
            );
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

<style>
/* .tb-edit .el-input {
    display: none
}
.tb-edit .current-row .el-input {
    display: block
}
.tb-edit .current-row .el-input+span {
    display: none
} */
</style>


