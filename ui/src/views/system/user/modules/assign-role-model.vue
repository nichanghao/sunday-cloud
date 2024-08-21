<script setup lang="ts">
import { computed, ref, shallowRef, watch } from "vue";
import { $t } from '@/locales';
import { assignRoleToUser, fetchGetAllRoles } from "@/service/api";


defineOptions({
  name: 'AssignRoleModel'
});

interface Props {
  /** the role list of user */
  roles: Api.SystemManage.Role[];
  /** the edit row data */
  /** the userId */
  userId: number;
}
const props = defineProps<Props>();

const visible = defineModel<boolean>('visible', {
  default: false
});

const title = computed(() => $t('page.manage.user.roleAuth'));


const roleIds = shallowRef<number[]>([]);


/** the enabled role options */
const roleOptions = ref<CommonType.Option<string>[]>([]);

async function getRoleOptions() {
  const { error, data } = await fetchGetAllRoles();

  if (!error) {
    const options = data.map(item => ({
      label: item.code,
      value: item.id
    }));

    roleOptions.value = [...options];
  }
}

function closeModal() {
  visible.value = false;
}

interface Emits {
  (e: 'submitted'): void;
}

const emit = defineEmits<Emits>();
async function handleSubmit() {
  // request
  const { error } = await assignRoleToUser(roleIds.value, props.userId)
  if (error) {
    return;
  }

  window.$message?.success?.($t('common.modifySuccess'));
  closeModal();
  emit('submitted');
}
function init() {
  roleIds.value = props.roles.map(item => item.id);
  getRoleOptions();
}

watch(visible, val => {
  if (val) {
    init();
  }
});
</script>
<template>
  <NModal v-model:show="visible" :title="title" preset="card" class="w-480px">
    <NSelect
      v-model:value="roleIds"
      multiple
      :options="roleOptions"
      :placeholder="$t('page.manage.user.form.userRole')"
    />
    <template #footer>
      <NSpace justify="end">
        <NButton size="small" class="mt-16px" @click="closeModal">
          {{ $t('common.cancel') }}
        </NButton>
        <NButton type="primary" size="small" class="mt-16px" @click="handleSubmit">
          {{ $t('common.confirm') }}
        </NButton>
      </NSpace>
    </template>
  </NModal>
</template>
