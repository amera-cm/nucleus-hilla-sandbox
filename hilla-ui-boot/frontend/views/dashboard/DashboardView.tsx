import {useEffect} from "react";

export default function DashboardView() {
    useEffect(() => {
        document.title = "Nucleus Hilla - Dashboard";
    });
    return (
        <div className="flex flex-col h-full items-center justify-center p-l text-center box-border">
            <h1>Dashboard</h1>
        </div>
    );
}
