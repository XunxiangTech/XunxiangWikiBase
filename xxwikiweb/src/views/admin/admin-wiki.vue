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
        <template v-slot:action="{text, record}">
          <a-space size="small">
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
      <a-form-item label="封面">
        <a-input v-model:value="wikibook.icon" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="wikibook.title" />
      </a-form-item>
      <a-form-item label="分类一">
        <a-input v-model:value="wikibook.category1Id" />
      </a-form-item>
      <a-form-item label="分类二">
        <a-input v-model:value="wikibook.category2Id" />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="wikibook.description" type="textarea" />
      </a-form-item>
    </a-form>

  </a-modal>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import { Tool } from '@/utils/tool';

export default defineComponent({
  name: 'AdminWiki',
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
        title: '分类一',
        key: 'category1Id',
        dataIndex: 'category1Id'
      },
      {
        title: '分类二',
        dataIndex: 'category2Id',
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

    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
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
    const wikibook = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
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

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return {
      param,
      wikiBooks,
      pagination,
      columns,
      loading,
      handleTableChange,
      handleQuery,

      edit,
      add,

      wikibook,
      modalVisible,
      modalLoading,
      handleModalOk,

      handleDelete
    }
  }
});
</script>

<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>
