<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          @click="handleClick"
      >
        <a-menu-item key="welcome">
          <TagsOutlined/>
          <span>欢迎</span>
        </a-menu-item>
        <a-sub-menu v-for="item in level1" :key="item.id" :disabled="false">
          <template v-slot:title>
            <span><DatabaseOutlined/> {{ item.name }}</span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">
            <FolderOpenOutlined/>
            <span>{{ child.name }}</span>
          </a-menu-item>
        </a-sub-menu>
        <a-menu-item key="tip" :disabled="true">
          <span>以上菜单在分类管理配置</span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
    <a-layout-content class="content"
                      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >

      <div class="welcome" v-show="isShowWelcome">
        <h1>欢迎来到寻向智库</h1>
      </div>
      <a-list v-show="!isShowWelcome" item-layout="vertical" size="large" :grid="{ gutter: 20, column: 3 }"
              :pagination="pagination" :data-source="companyWiki">
        <template #renderItem="{ item }">
          <a-list-item key="item.title">
            <template #actions>
              <span>
                <component v-bind:is="'FileOutlined'" style="margin-right: 8px"/>
                {{ item.docCount }}
              </span>
              <span>
                <component v-bind:is="'UserOutlined'" style="margin-right: 8px"/>
                {{ item.viewCount }}
              </span>
              <span>
                <component v-bind:is="'LikeOutlined'" style="margin-right: 8px"/>
                {{ item.voteCount }}
              </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <router-link :to="'/doc?wikibookId=' + item.id">
                  {{ item.title }}
                </router-link>
              </template>
              <template #avatar>
                <a-avatar :src="item.icon"/>
              </template>
            </a-list-item-meta>
            {{ item.content }}
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref, reactive, toRef} from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from '@/utils/tool';

export default defineComponent({
  name: 'Home',
  setup() {
    const companyWiki = ref();
    // const companyWiki1 = reactive({wikis: []});

    const level1 = ref();
    let categorys: any;
    /**
     * 查询所有分类
     **/
    const handleQueryCategory = () => {
      axios.get("/category/all").then((response) => {
        const data = response.data;
        if (data.success) {
          categorys = data.content;
          console.log("原始数组：", categorys);

          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);
          console.log("树形结构：", level1.value);

        } else {
          message.error(data.message);
        }
      });
    };

    const isShowWelcome = ref(true);
    let categoryId2 = 0;

    const handleQueryWikibook = () => {
      axios.get("/wikibook/list", {
        params: {
          page: 1,
          size: 5,
          category2Id: categoryId2
        }
      }).then((response) => {
        const data = response.data;
        companyWiki.value = data.content.list;
        //ebook1.books = data.content;
      });
    };

    const handleClick = (value: any) => {
      if (value.key == 'welcome') {
        isShowWelcome.value = true;
      } else {
        categoryId2 = value.key;
        isShowWelcome.value = false;
        handleQueryWikibook();
      }
      console.log("menu click", value);
    };

    onMounted(() => {
      handleQueryCategory();
    });

    const pagination = {
      // DIFF: (page: any)
      onChange: (page: number) => {
        console.log(page);
      },
      pageSize: 3,
    };
    // const actions: Record<string, string>[] = [
    //   {type: 'StarOutlined', text: '156'},
    //   {type: 'LikeOutlined', text: '156'},
    //   {type: 'MessageOutlined', text: '2'},
    // ];

    return {
      companyWiki,
      // companyWiki2: toRef(companyWiki1, "wikis"),
      handleQueryWikibook,
      handleClick,
      handleQueryCategory,

      pagination,
      // actions,
      isShowWelcome,
      level1
    }
  }
});
</script>

<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>
