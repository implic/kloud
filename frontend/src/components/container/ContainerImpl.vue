<template>
  <el-card class="box-card">
    <el-row type="flex" justify="start">
      <create-pod :course="$props.course" :submitCallback="getPodList"
                  v-if="courseId == '0' || userType !== '0'"
      ></create-pod>
      <el-button icon="el-icon-refresh" @click="getPodList">刷新</el-button>
      <el-input
          placeholder="搜索"
          prefix-icon="el-icon-search"
          v-model="searchInput"
          clearable
          style="margin-left: 40%"
      ></el-input>
    </el-row>

    <el-table :data="searchResult" stripe style="width: 100%">
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column prop="image" label="镜像"></el-table-column>
      <el-table-column prop="memUsage" label="内存占用（MiB）">
      </el-table-column>
      <el-table-column prop="status" label="状态"></el-table-column>
      <el-table-column prop="action" label="操作" width="240%">
        <template slot-scope="scope">
          <pod-log :course="$props.course" :podName="scope.row.name"
                   ></pod-log>
          <pod-shell
              :course="$props.course"
              :podName="scope.row.name"
          ></pod-shell>
          <delete-pod
              :course="$props.course"
              :podName="scope.row.name"
              :submitCallback="getPodList" v-if="courseId === '0' || userType
               !== '0'"
          ></delete-pod>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script>
import CreatePod from "./components/CreatePod.vue";
import PodLog from "./components/PodLog.vue";
import PodShell from "./components/PodShell.vue";
import DeletePod from "./components/DeletePod.vue";

export default {
  name: "container-impl",
  props: {
    course: String
  },
  data() {
    return {
      searchInput: "",
      podList: [],
      courseId: '',
      userType: ''
    };
  },
  computed: {
    searchResult: function () {
      let search = this.searchInput.toLowerCase();
      if (search) {
        return this.podList.filter((podInfo) => {
          return Object.keys(podInfo).some((key) => {
            return String(podInfo[key]).toLowerCase().indexOf(search) > -1;
          });
        });
      }
      return this.podList;
    },
  },
  components: {
    CreatePod,
    PodLog,
    PodShell,
    DeletePod,
  },
  mounted() {
    this.getPodList();
    this.userType = window.sessionStorage.getItem('userType')
    this.courseId = this.$props.course
  },
  methods: {
    getPodList() {
      let user = JSON.parse(window.sessionStorage.getItem("user"));
      this.$axios
          .get("/pod/list", {
            params: {
              id: user.login,
              course: this.$props.course
            },
          })
          .then((successResp) => {
            if (successResp.data) {
              this.podList = successResp.data;
            }
          });
    },
  },
};
</script>
<style>
.el-row {
  margin-bottom: 20px;
}

.el-button + .el-button {
  margin-left: 0 !important;
}

.el-button {
  margin-right: 10px !important;
}

.el-button:last-child {
  margin-right: 0;
}
</style>