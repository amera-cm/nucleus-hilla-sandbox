import React from 'react';
import {MenuProps, routes, ViewRouteObject} from 'Frontend/routes.js';
import {NavLink} from 'react-router-dom';
import {Item} from '@vaadin/react-components/Item.js';
import {Scroller} from '@vaadin/react-components/Scroller.js';
import {Icon} from '@vaadin/react-components/Icon.js';
import reactIcon from 'Frontend/themes/hilla/line-awesome/svg/react.svg';
import css from './MainDrawer.module.css';

type MenuRoute = ViewRouteObject &
    Readonly<{
        path: string;
        handle: Required<MenuProps>;
    }>;

export default function MainDrawer() {
    const menuRoutes = (routes[0]?.children || []).filter(
        (route) => route.path && route.handle && route.handle.icon && route.handle.caption
    ) as readonly MenuRoute[];

    return (
        <Scroller scroll-direction="vertical">
            <nav>
                {menuRoutes.map(({path, handle: {icon, caption}}) => (
                    <NavLink
                        className={({isActive}) => `${css.navlink} ${isActive ? css.navlink_active : ''}`}
                        key={path}
                        to={path}
                    >
                        {({isActive}) => (
                            <Item key={path} selected={isActive}>
                                <Icon src={reactIcon}/>
                                {caption}
                            </Item>
                        )}
                    </NavLink>
                ))}
            </nav>
        </Scroller>
    );
}