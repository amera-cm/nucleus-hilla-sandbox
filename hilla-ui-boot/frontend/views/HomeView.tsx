import {useEffect} from "react";

export default function HomeView() {
    useEffect(() => {
        document.title = "Nucleus Hilla - Home";
    });
    return (
        <div className="flex flex-col h-full items-center justify-center p-l text-center box-border">
            <h1>Home</h1>
        </div>
    );
}
