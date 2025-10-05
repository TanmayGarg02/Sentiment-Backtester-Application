import { motion } from "framer-motion";
import { BarChart3, LineChart, History, Cpu } from "lucide-react";

export default function Home() {
  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gradient-to-br from-[#f5f1e6] to-[#e7dfcf] text-[#3a2e25] px-6">
      <motion.h1
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.8 }}
        className="text-5xl font-extrabold text-center mb-6"
      >
        Welcome to <span className="text-green-700">QuantDash</span>
      </motion.h1>

      <motion.p
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.3, duration: 0.8 }}
        className="text-xl text-center max-w-2xl mb-12"
      >
        QuantDash is a trading analytics platform where you can:
      </motion.p>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-10 text-lg">
        <motion.div
          whileHover={{ scale: 1.05 }}
          className="p-6 bg-white rounded-2xl shadow-lg text-center"
        >
          <BarChart3 className="mx-auto w-12 h-12 text-green-700 mb-3" />
          <p>📊 Analyze stock sentiments in real time</p>
        </motion.div>

        <motion.div
          whileHover={{ scale: 1.05 }}
          className="p-6 bg-white rounded-2xl shadow-lg text-center"
        >
          <Cpu className="mx-auto w-12 h-12 text-green-700 mb-3" />
          <p>⚡ Run backtests on different strategies</p>
        </motion.div>

        <motion.div
          whileHover={{ scale: 1.05 }}
          className="p-6 bg-white rounded-2xl shadow-lg text-center"
        >
          <History className="mx-auto w-12 h-12 text-green-700 mb-3" />
          <p>📜 View your complete backtest history</p>
        </motion.div>
      </div>
    </div>
  );
}
