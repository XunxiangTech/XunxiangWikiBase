<template>
  <a-layout>
    <a-layout-content class="content"
                      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
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
            <a-button danger shape="round">
              删除
            </a-button>
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

export default defineComponent({
  name: 'AdminWiki',
  setup() {
    const wikiBooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 4,
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
          size: params.size
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        wikiBooks.value = data.content.list;

        // 重置分页按钮
        pagination.value.current = params.page;
        pagination.value.total = data.content.total;
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
      setTimeout(() => {
        modalVisible.value = false;
        modalLoading.value = false;
      }, 2000)
    }

    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      wikibook.value = record;
    }

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return {
      wikiBooks,
      pagination,
      columns,
      loading,
      handleTableChange,

      edit,

      wikibook,
      modalVisible,
      modalLoading,
      handleModalOk
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
