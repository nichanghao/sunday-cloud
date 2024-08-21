import { useAuthStore } from '@/store/modules/auth';
import { useRouteStore } from '@/store/modules/route';

export function useAuth() {
  const authStore = useAuthStore();
  const routeStore = useRouteStore();

  function hasAuth(codes: string | string[]) {
    if (!authStore.isLogin) {
      return false;
    }

    if (typeof codes === 'string') {
      return routeStore.permissionSet.has(codes);
    }

    return codes.some(code => routeStore.permissionSet.has(code));
  }

  return {
    hasAuth
  };
}
