
<template>
  <div class="main-content">
    <!--整体的大盒子-->
    <div style="width: 70%; margin: 20px auto">
      <!--font-width:550  字体加粗-->
      <div style="color: #8b4513;font-size:16px;font-weight: 550">快来看看有没有你丢失的物品吧！如果有，请记得答谢一下帮你找到的小伙伴哦~</div>
      <!--常见的一个输入框和两个按钮，在后台有一样的格式，直接copy修改一下-->
      <div style="margin-top: 30px">
        <el-input placeholder="请输入物品名称查询" style="width: 200px" v-model="name" size="mini"></el-input>
        <el-button type="info" style="margin-left: 10px" @click="load(1)" size="mini">查询</el-button>
        <el-button type="warning" style="margin-left: 10px" @click="reset" size="mini">重置</el-button>
      </div>
      <!--每行四个数据-->
      <div style="margin-top: 20px">
        <el-row :gutter="20">
          <el-col :span="6" v-for="item in foundData" style="margin-bottom: 20px">
            <div class="transition card">
              <div style="display: flex" >
                <img :src=" item.img" style="height: 75px;width: 75px;border: 1px solid #eeeeee; border-radius:10px">
                <div style="margin-left: 10px">
                  <div style="font-weight: 550;color: #ef5d26">{{ item.name }}</div>
                  <div style="margin-top: 10px;color: #666666">发布人：{{ item.twoName }}</div>
                  <div style="margin-top: 5px;color: #666666">时间：{{ item.time }}</div>
                </div>
              </div>
              <div style="margin-top: 20px">
                <el-button type="info" size="mini" @click="viewContent(item.content)">查看详情</el-button>
                <el-button type="success" size="mini" @click="contact(item)">联系他</el-button>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <!--分页-->
      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
      <!--查看详情-->
      <el-dialog title="详细信息" :visible.sync="viewVisible" width="55%" :close-on-click-modal="false" destroy-on-close>
        <div v-html="viewData" class="w-e-text w-e-text-container"></div>
      </el-dialog>
      <el-dialog title="信息" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
        <el-form label-width="100px" style="padding-right: 50px" >
          <el-form-item prop="title" label="友情提示">
            <div style="color: #ff0000">请详细描述你的联系方式，方便对方联系到你</div>
          </el-form-item>
          <el-form-item prop="content" label="留言内容">
            <el-input type="textarea" :rows="5" v-model="content" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="fromVisible = false">取 消</el-button>
          <el-button type="primary" @click="submit">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import E from 'wangeditor'
export default {

  //一些数据存放的地址，比如轮播图的图片地址
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      pageNum:1,
      pageSize:8,
      total:0,
      name:null,
      foundData:[],
      viewVisible:false,
      viewData:null,
      fromVisible:false,
      content:null,
      form : {}
    }
  },
  mounted() {
    this.load(1)  //打开页面就默认加载第一页的数据展示
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/found/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.foundData = res.data?.list
        this.total = res.data?.total
      })
    },
    handleCurrentChange(pageNum){
      this.pageNum = pageNum
      this.load(this.pageNum)
    },
    reset(){
      this.name = null
      this.load(1)
    },
    viewContent(content){
      this.viewData = content
      this.viewVisible = true
    },
    contact(item){
      this.form = JSON.parse(JSON.stringify(item))
      this.fromVisible = true
    },
    submit(){
      if (this.user.id === this.form.userId){
        this.$message.warning('您不能联系自己')
        this.content = null
        return
      }
      let data = {
        articleID : this.form.id,
        type : '招领广场',
        fromID : this.user.id,
        toId : this.form.userId,
        content : this.content
      }
      this.$request.post('/message/add',data).then(res =>{
        if (res.code === '200'){
          this.$message.success('留言成功，等待对方联系')
          this.content = null
          this.fromVisible = false
        }else {
          this.$message.error(res.msg)
        }
      })
    },
  }
}
</script>
