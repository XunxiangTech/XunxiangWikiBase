<template>
    <a-layout>
        <a-layout-content :style="{ background:'#fff', padding: '20px 24px', margin: '24px', minHeight: '280px' }">
            <p>
                <a-form layout="inline" :model="param">
                    <a-form-item>
                        <a-button type="primary" @click="add()" shape="round">
                            新增
                        </a-button>
                    </a-form-item>
                </a-form>
            </p>
            <a-table
                    :columns="columns"
                    :row-key="record => record.id"
                    :data-source="level1"
                    :loading="loading"
                    :pagination="false"
                    :defaultExpandAllRows="true"
            >
                <template v-slot:action="{ text, record }">
                    <a-space size="small">
                        <router-link :to="'/admin/doc?categoryId=' + record.id">
                            <a-button type="primary" shape="round">
                                文档管理
                            </a-button>
                        </router-link>
                        <a-button type="primary" @click="edit(record)" shape="round">
                            编辑
                        </a-button>
                        <a-popconfirm
                                title="删除后不可恢复，确认删除?"
                                ok-text="是"
                                cancel-text="否"
                                @confirm="handleDelete(record.id)"
                        >
                            <a-button type="danger" shape="round">
                                删除
                            </a-button>
                        </a-popconfirm>
                    </a-space>
                </template>
            </a-table>
        </a-layout-content>
    </a-layout>
    <a-modal
            title="分类表单"
            v-model:visible="modalVisible"
            :confirm-loading="modalLoading"
            @ok="handleModalOk"
    >
        <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
            <a-form-item label="名称">
                <a-input v-model:value="category.name" />
            </a-form-item>
            <a-form-item label="父分类">
                <a-input v-model:value="category.parent" />
                <a-select
                        v-model:value="category.parent"
                        ref="select"
                >
                    <a-select-option :value="0">
                        无
                    </a-select-option>
                    <a-select-option v-for="c in level1" :key="c.id" :value="c.id" :disabled="category.id === c.id">
                        {{c.name}}
                    </a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="顺序">
                <a-input v-model:value="category.sort" />
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script lang="ts">
    import { defineComponent, onMounted, ref } from 'vue';
    import axios from 'axios';
    import { message } from 'ant-design-vue';
    import {Tool} from "@/utils/tool";

    export default defineComponent({
        name:'AdminCategory',
        setup(){
            const param = ref();
            param.value = {};
            let categorys:any;
            const loading = ref(false);

            const columns = [
                {
                    title: '名称',
                    dataIndex: 'name'
                },
                {
                    title: '父分类',
                    key:'parent',
                    dataIndex:'parent',
                    slots: {customRender: 'parent'}
                },
                {
                    title: '顺序',
                    dataIndex: 'sort'
                },
                {
                    title: '操作',
                    key: 'action',
                    slots: { customRender: 'action' }
                }
            ];
            /**
             * 一级分类树，children属性就是二级分类
             * [{
             *   id: "",
             *   name: "",
             *   children: [{
             *     id: "",
             *     name: "",
             *   }]
             * }]
             */
            const level1 = ref(); // 一级分类树，children属性就是二级分类
            const category = ref({});
            /**
             * 数据查询
             **/
            const handleQuery = () => {
                loading.value = true;
                // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
                level1.value=[];
                axios.get("category/all",).then((response)=>{
                    loading.value = false;
                    const data = response.data;
                    if(data.success){
                        categorys = data.content;
                        level1.value=Tool.array2Tree(categorys,0);
                        console.log("原始数组：",category.value);
                        console.log("树形结构：",level1);
                    }
                    else{
                        message.error(data.message);
                    }
                });
            };

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

            const modalVisible = ref(false);
            /**
             * 新增
             */
            const add = () => {
                modalVisible.value = true;
                category.value = {};
            };

            onMounted(() => {
                handleQuery();

            });

            return{
                param,
                handleQuery,
                columns,
                level1,
                loading,
                getCategoryName,
                add,
                modalVisible,
                category
            }
        }
    })

</script>