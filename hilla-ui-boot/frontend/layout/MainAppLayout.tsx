import React, {Suspense, useRef} from 'react';
import {AppLayout} from '@vaadin/react-components/AppLayout.js';
import {DrawerToggle} from '@vaadin/react-components/DrawerToggle.js';
import {type SideNavElement} from '@vaadin/react-components/SideNav.js';
import {Outlet} from 'react-router-dom';
import Placeholder from 'Frontend/components/placeholder/Placeholder.js';
import MainDrawer from 'Frontend/components/mainDrawer/MainDrawer.js';
import '@vaadin/icons/';

const h1Style = {
    fontSize: 'var(--lumo-font-size-l)',
    margin: 0,
};

export default function MainAppLayout() {
    const sideNavRef = useRef<SideNavElement>(null);
    return (
        <AppLayout>
            <DrawerToggle slot="navbar"/>
            <h1 slot="navbar" style={h1Style}>
                Nucleus Hilla
            </h1>
            <div slot="drawer" className="mainDrawerWrapper h-full w-full">
                <MainDrawer/>
            </div>

            <Suspense fallback={<Placeholder/>}>
                <Outlet/>
            </Suspense>
        </AppLayout>
    );
}
