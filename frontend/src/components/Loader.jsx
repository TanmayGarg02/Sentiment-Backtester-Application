import { Loader2 } from "lucide-react";

export default function Loader() {
  return (
    <div className="flex justify-center items-center py-10">
      <Loader2 className="animate-spin text-blue-500" size={40} />
    </div>
  );
}
