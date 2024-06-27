import {useEffect} from "react";

export default function AdminView() {
    useEffect(() => {
        document.title = "Nucleus Hilla - Admin";
    });
    return (
        <div className="flex flex-col h-full items-center justify-center p-l text-center box-border">
            <h1>Admin</h1>
        </div>
    );
}
