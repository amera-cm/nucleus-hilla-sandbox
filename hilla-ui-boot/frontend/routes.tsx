import HelloReactView from 'Frontend/views/helloreact/HelloReactView.js';
import MainAppLayout from 'Frontend/layout/MainAppLayout.js';
import HomeView from 'Frontend/views/HomeView.js';
import AdminView from 'Frontend/views/admin/AdminView.js';
import DashboardView from 'Frontend/views/dashboard/DashboardView.js';
import {lazy} from 'react';
import {createBrowserRouter, IndexRouteObject, NonIndexRouteObject, useMatches} from 'react-router-dom';

const AboutView = lazy(async () => import('Frontend/views/about/AboutView.js'));
export type MenuProps = Readonly<{
    icon?: string;
    caption?: string;
}>;

export type ViewMeta = Readonly<{ handle?: MenuProps }>;

type Override<T, E> = Omit<T, keyof E> & E;

export type IndexViewRouteObject = Override<IndexRouteObject, ViewMeta>;
export type NonIndexViewRouteObject = Override<
    Override<NonIndexRouteObject, ViewMeta>,
    {
        children?: ViewRouteObject[];
    }
>;
export type ViewRouteObject = IndexViewRouteObject | NonIndexViewRouteObject;

type RouteMatch = ReturnType<typeof useMatches> extends (infer T)[] ? T : never;

export type ViewRouteMatch = Readonly<Override<RouteMatch, ViewMeta>>;

export const useViewMatches = useMatches as () => readonly ViewRouteMatch[];

export const routes: readonly ViewRouteObject[] = [
    {
        element: <MainAppLayout/>,
        handle: {icon: 'null', caption: 'Main'},
        children: [
            {path: '/', element: <HomeView/>, handle: {icon: 'vaadin:home-o', caption: 'Home'}},
            {path: '/hello', element: <HelloReactView/>, handle: {icon: 'vaadin:alarm', caption: 'Hello React'}},
            {path: '/dashboard', element: <DashboardView/>, handle: {icon: 'vaadin:spline-chart', caption: 'Dashboard'}},
            {path: '/about', element: <AboutView/>, handle: {icon: 'vaadin:info-circle-o', caption: 'About'}},
            {path: '/admin', element: <AdminView/>, handle: {icon: 'vaadin:cog-o', caption: 'Admin'}},
        ],
    },
];

const router = createBrowserRouter([...routes], {basename: '/ui'} as any);
export default router;
