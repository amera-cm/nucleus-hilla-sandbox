import React, {useRef} from 'react';
import {SideNav, type SideNavElement} from '@vaadin/react-components/SideNav.js';
import {SideNavItem} from '@vaadin/react-components/SideNavItem.js';
import {Icon} from '@vaadin/react-components/Icon.js';
import router, {MenuProps, routes, ViewRouteObject} from 'Frontend/routes.js';
import {VerticalLayout} from '@vaadin/react-components/VerticalLayout.js';
import '@vaadin/icons/';

type MenuRoute = ViewRouteObject &
    Readonly<{
        path: string;
        handle: Required<MenuProps>;
    }>;

export default function MainDrawer() {
    const sideNavRef = useRef<SideNavElement>(null);
    const menuRoutes = (routes[0]?.children || []).filter(
        (route) => route.path && route.handle && route.handle.icon && route.handle.caption
    ) as readonly MenuRoute[];

    return (
        <VerticalLayout>
            <SideNav className="h-full w-full" id="sideNav" ref={sideNavRef}>
                {menuRoutes.map(({path, handle: {icon, caption}}) => (
                    <SideNavItem path={router.basename + path}>
                        <Icon icon={icon} slot="prefix"/>
                        {caption}
                    </SideNavItem>
                ))}
            </SideNav>
        </VerticalLayout>
    );
}