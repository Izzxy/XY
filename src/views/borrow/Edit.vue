<template>
<div style="width: 80%">
    <div style="margin-bottom: 30px">编辑图书</div>
  <el-form :inline="true" :rules="rules" ref="ruleForm" :model="form"  label-width="100px">
    <el-form-item label="图书的标准码" prop="bookNo" style="margin-left: 25px">
      <el-select v-model="form.bookNo" clearable filterable placeholder="请选择" @change="selBook">
        <el-option
            v-for="item in books"
            :key="item.id"
            :label="item.bookNo"
            :value="item.bookNo">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="图书名称" prop="bookName">
      <el-input v-model="form.bookName" disabled></el-input>
    </el-form-item>
    <el-form-item label="所需积分" prop="score">
      <el-input v-model="form.score" disabled></el-input>
    </el-form-item>
    <el-form-item label="图书数量" prop="nums">
      <el-input v-model="form.nums" disabled></el-input>
    </el-form-item>
    <br />
    <el-form-item label="用户会员码" prop="userNo" style="margin-left: 25px">
      <el-select v-model="form.userNo" filterable placeholder="请选择" @change="selUser">
        <el-option
            v-for="item in users"
            :key="item.id"
            :label="item.username"
            :value="item.username">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="用户名称" prop="userName" style="margin-left: 25px">
      <el-input v-model="form.userName" disabled> </el-input>
    </el-form-item>
    <el-form-item label="用户联系方式" prop="userPhone" style="margin-left: 25px">
      <el-input v-model="form.userPhone" disabled> </el-input>
    </el-form-item>
    <el-form-item label="用户账号积分" prop="account" style="margin-left: 25px">
      <el-input v-model="form.account" disabled> </el-input>
    </el-form-item>
    <el-form-item label="借出天数" prop="days" style="margin-left: 25px">
      <el-input-number v-model="form.days" :min="1" :max="30" label="所需积分"></el-input-number>
    </el-form-item>
  </el-form>

  <div style="text-align: center; margin-top: 30px">
    <el-button type="primary" @click="save" size="medium">提交</el-button>
  </div>
</div>
</template>

<script>
import request from "@/utils/request";
export default {
  name: "EditBook",
  data() {
   return {
     form: {},
     books: [],
     users: [],
     rules: {
       bookNo: [
         {required: true, message: '请输入图书标准码', trigger: 'blur'},
       ],
       userNo: [
         {required: true, message: '请输入用户会员码', trigger: 'blur'},
       ]
     }
   }
  },
  created() {
    request.get('/book/list').then(res => {
      this.books = res.data
    })

    request.get('/user/list').then(res => {
      this.users = res.data.filter(v => v.status)
    })

    const id = this.$route.query.id
    request.get("/borrow/" + id).then(res =>{
      this.form = res.data
    })
  },
  methods: {
    save() {
      request.put('/borrow/update',this.form).then(res => {
        if (res.code === '200'){
          this.$notify.success('更新成功')
          this.$router.push("/bookList")
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    selBook() {
      const book = this.books.find(v => v.bookNo === this.form.bookNo)
      request.get('/book/' + book.id).then(res => {
        this.form.bookName = res.data.name
        this.form.score = res.data.score
        this.form.nums = res.data.nums
      })
    },
    selUser() {
      const user = this.users.find(v => v.username === this.form.userNo)
      request.get('/user/' + user.id).then(res => {
        this.form.userName = res.data.name
        this.form.userPhone = res.data.phone
        this.form.account = res.data.account
      })
    }
  }
}
</script>

<style scoped>

</style>