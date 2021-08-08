<template>
  <a-layout>
    <a-layout-content class="content"
                      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-input v-model:value="param.title" placeholder="名称"></a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
              查询
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add">
              新增
            </a-button>
          </a-form-item>
        </a-form>
      </p>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="wikiBooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #icon="{ text: icon }">
          <img v-if="icon" :src="icon" alt="icon"/>
        </template>
        <template v-slot:category="{ text, record }">
          <span>{{ getCategoryName(record.category1Id) }} / {{ getCategoryName(record.category2Id) }}</span>
        </template>
        <template v-slot:action="{text, record}">
          <a-space size="small">
            <router-link :to="'/admin/doc?wikibookId=' + record.id">
              <a-button type="primary" shape="round">
                文档管理
              </a-button>
            </router-link>
            <a-button type="primary" shape="round" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="删除后不可恢复，确认删除？"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.id)"
            >
              <a-button danger shape="round">
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
      title="百科书表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="wikibook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
<!--      <a-form-item label="封面">-->
<!--        <a-input v-model:value="wikibook.icon"/>-->
<!--      </a-form-item>-->
      <a-form-item label="封面">
        <a-space>
          <a-upload
                  v-model:file-list="fileList"
                  name="avatar"
                  list-type="picture-card"
                  class="avatar-uploader"
                  :show-upload-list="false"
                  :before-upload="beforeUpload"
                  action="http://127.0.0.1:8880/wikibook/ex"
                  @change="handleChange"
          >
            <img v-if="imageUrl" :src="imageUrl" alt="avatar" />
            <div v-else>
              <LoadingOutlined v-if="loading1"></LoadingOutlined>
              <!--            <loading-outlined v-if="loading"></loading-outlined>-->
              <PlusOutlined v-else></PlusOutlined>
              <!--            <plus-outlined v-else></plus-outlined>-->
              <div class="ant-upload-text">Upload</div>
            </div>
          </a-upload>
          <a-button type="primary" ghost @click="saveImage(image)">确认上传该封面照片</a-button>
        </a-space>

      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="wikibook.title"/>
      </a-form-item>
      <a-form-item label="分类">
        <a-cascader
            v-model:value="categoryIds"
            :field-names="{ label: 'name', value: 'id', children: 'children' }"
            :options="level1"
        />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="wikibook.description" type="textarea"/>
      </a-form-item>
    </a-form>

  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from '@/utils/tool';
import { PlusOutlined, LoadingOutlined } from '@ant-design/icons-vue';

interface FileItem {
  uid: string;
  name?: string;
  status?: string;
  response?: string;
  url?: string;
  type?: string;
  size: number;
  originFileObj: any;
}

interface FileInfo {
  file: FileItem;
  fileList: FileItem[];
}
function getBase64(img: Blob, callback: (base64Url: string) => void) {
  const reader = new FileReader();
  reader.addEventListener('load', () => callback(reader.result as string));
  reader.readAsDataURL(img);
}

export default defineComponent({
  name: 'AdminWiki',
  components: {
    LoadingOutlined,
    PlusOutlined,
  },
  setup() {
    const param = ref();
    param.value = {};
    const wikiBooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: '封面',
        dataIndex: 'icon',
        slots: {customRender: 'icon'}
      },
      {
        title: '名称',
        dataIndex: 'title'
      },
      {
        title: '分类',
        slots: {customRender: 'category'},
      },
      {
        title: '文档数',
        dataIndex: 'docCount'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount'
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount'
      },
      {
        title: '操作',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];

    let file: any;

    const fileList = ref([]);
    const loading1 = ref<boolean>(false);
    const imageUrl = ref<string>('');

    const handleChange = (info: FileInfo) => {
      if (info.file.status === 'uploading') {
        loading1.value = true;
        return;
      }
      if (info.file.status === 'done') {
        // Get this url from response in real world.
        getBase64(info.file.originFileObj, (base64Url: string) => {
          wikibook.value.icon = info.file.name;
          imageUrl.value = base64Url;
          loading1.value = false;
        });
        file = info.file.originFileObj;
      }
      if (info.file.status === 'error') {
        loading1.value = false;
        console.log(info.file.response)
        message.error('upload error');
      }
    };

    const beforeUpload = (file: FileItem) => {
      const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
      if (!isJpgOrPng) {
        message.error('You can only upload JPG file!');
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        message.error('Image must smaller than 2MB!');
      }
      return isJpgOrPng && isLt2M;
    };

    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      // UPDATE: 可能不需要
      wikiBooks.value = [];
      axios.get("/wikibook/list", {
        params: {
          page: params.page,
          size: params.size,
          title: param.value.title
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          wikiBooks.value = data.content.list;

          // 重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    /**
     * 表单
     */
        // 数组 [100, 101]对应：前端开发 / Vue
    const categoryIds = ref();
    const wikibook = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      wikibook.value.category1Id = categoryIds.value[0];
      wikibook.value.category2Id = categoryIds.value[1];
      axios.post("/wikibook/save", wikibook.value).then((response) => {
        modalLoading.value = false;
        const data = response.data; // data = commonResp
        if (data.success) {
          modalVisible.value = false;

          // 重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        } else {
          message.error(data.message);
        }
      })
    }

    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      wikibook.value = Tool.copy(record);
      categoryIds.value = [wikibook.value.category1Id, wikibook.value.category2Id];
    }
    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      wikibook.value = {};
    }
    /**
     * 删除
     */
    const handleDelete = (id: number) => {
      axios.delete("/wikibook/delete/" + id).then((response) => {
        const data = response.data; // data = commonResp
        if (data.success) {
          // 重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        }
      })
    }

    const level1 = ref();
    let categorys: any;
    /**
     * 查询所有分类
     **/
    const handleQueryCategory = () => {
      loading.value = true;
      axios.get("/category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categorys = data.content;
          console.log("原始数组：", categorys);

          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);
          console.log("树形结构：", level1.value);

          // 加载完分类后，再加载电子书，否则如果分类树加载很慢，则电子书渲染会报错
          handleQuery({
            page: 1,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(data.message);
        }
      });
    };

    const saveImage = ()=>{
      const formData = new FormData()
      formData.append('file',file);
      axios.post("wikibook/upload-avatar",formData).then((response)=>{
        console.log("OK");
      });
    }

    const getCategoryName = (cid: number) => {
      // console.log(cid)
      let result = "";
      categorys.forEach((item: any) => {
        if (item.id === cid) {
          // return item.name; // 注意，这里直接return不起作用
          result = item.name;
        }
      });
      return result;
    };

    onMounted(() => {
      handleQueryCategory();
    });

    return {
      param,
      wikiBooks,
      pagination,
      columns,
      loading,
      loading1,
      handleTableChange,
      handleQuery,
      getCategoryName,

      edit,
      add,

      wikibook,
      modalVisible,
      modalLoading,
      handleModalOk,
      categoryIds,
      level1,
      fileList,
      imageUrl,

      handleDelete,
      handleChange,
      beforeUpload,
      saveImage
    }
  }
});
</script>

<style scoped>
img {
  width: 50px;
  height: 50px;
}
.avatar-uploader > .ant-upload {
  width: 128px;
  height: 128px;
}
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>
