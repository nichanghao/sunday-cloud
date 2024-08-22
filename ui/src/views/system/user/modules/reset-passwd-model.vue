<script setup lang="ts">
import { computed, shallowRef, watch } from "vue";
import { $t } from '@/locales';
import { resetUserPwd } from "@/service/api";


defineOptions({
  name: 'ResetPasswdModel'
});

interface Props {
  userId: number;
}
const props = defineProps<Props>();

const visible = defineModel<boolean>('visible', {
  default: false
});

const title = computed(() => $t('page.manage.user.resetPassWd'));


const password = shallowRef<string>('');

function closeModal() {
  visible.value = false;
}

async function handleSubmit() {
  // request
  const { error } = await resetUserPwd(password.value, props.userId)
  if (error) {
    return;
  }
  window.$message?.success?.($t('common.modifySuccess'));

  closeModal();
}

watch(visible, () => {
  if (visible.value) {
    password.value = '';
  }
});
</script>

<template>
  <NModal v-model:show="visible" :title="title" preset="card" class="w-480px">
    <NInput v-model:value="password" :placeholder="$t('page.manage.user.form.password')" />
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
