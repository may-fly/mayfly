<template>
    <div v-if="getPermissions">
        <slot />
    </div>
</template>

<script lang="ts">
import { computed } from 'vue';
import { useStore } from '/@/store/index.ts';
export default {
    name: 'auth',
    props: {
        value: {
            type: String,
            default: () => '',
        },
    },
    setup(props) {
        const store = useStore();
        // 获取 vuex 中的用户权限
        const getPermissions = computed(() => {
            return store.state.userInfos.userInfos.permissions.some((v: any) => v === props.value);
        });
        return {
            getPermissions,
        };
    },
};
</script>
