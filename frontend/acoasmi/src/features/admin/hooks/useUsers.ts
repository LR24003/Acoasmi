import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { userService } from '../services/userService';
import type { UsuariosRequestDTO } from '../types';

export function useUsers() {
    const queryClient = useQueryClient();

    const usersQuery = useQuery({
        queryKey: ['users'],
        queryFn: userService.getUsers,
    });

    const rolesQuery = useQuery({
        queryKey: ['roles'],
        queryFn: userService.getRoles,
    });

    const createUserMutation = useMutation({
        mutationFn: (dto: UsuariosRequestDTO) => userService.createUser(dto),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['users'] });
        },
    });

    const updateUserMutation = useMutation({
        mutationFn: ({ id, dto }: { id: number; dto: UsuariosRequestDTO }) =>
            userService.updateUser(id, dto),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['users'] });
        },
    });

    const deleteUserMutation = useMutation({
        mutationFn: (id: number) => userService.deleteUser(id),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['users'] });
        },
    });

    return {
        users: usersQuery.data ?? [],
        roles: rolesQuery.data ?? [],
        isLoading: usersQuery.isLoading || rolesQuery.isLoading,
        isError: usersQuery.isError,
        createUser: createUserMutation.mutateAsync,
        updateUser: updateUserMutation.mutateAsync,
        deleteUser: deleteUserMutation.mutateAsync,
        isSubmitting:
            createUserMutation.isPending ||
            updateUserMutation.isPending ||
            deleteUserMutation.isPending,
    };
}