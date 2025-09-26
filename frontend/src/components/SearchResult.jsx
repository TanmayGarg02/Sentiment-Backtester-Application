import React from "react";
import { useSelector } from "react-redux";

export default function SentimentResults() {
  const { data, loading, error } = useSelector((state) => state.sentiment);

  if (loading) return <p className="text-gray-600">Loading sentiment...</p>;
  if (error) return <p className="text-red-500">Error: {error}</p>;
  if (!data) return <p className="text-gray-500">No sentiment data yet.</p>;

  return (
    <div>
      <h2 className="text-xl font-semibold mb-3">Results for {data.ticker}</h2>

      {/* News Sentiment */}
      <div className="mb-4">
        <h3 className="font-medium text-gray-700">📰 News</h3>
        {data.news && data.news.length > 0 ? (
          <ul className="space-y-2 mt-2">
            {data.news.map((article, idx) => (
              <li
                key={idx}
                className="border rounded-lg p-3 hover:bg-gray-50"
              >
                <a
                  href={article.url}
                  target="_blank"
                  rel="noreferrer"
                  className="font-medium text-blue-600 hover:underline"
                >
                  {article.title}
                </a>
                <p className="text-sm text-gray-500">
                  Sentiment:{" "}
                  <span
                    className={
                      article.sentiment.label === "positive"
                        ? "text-green-600"
                        : article.sentiment.label === "negative"
                        ? "text-red-600"
                        : "text-gray-600"
                    }
                  >
                    {article.sentiment.label} ({article.sentiment.score.toFixed(2)})
                  </span>
                </p>
              </li>
            ))}
          </ul>
        ) : (
          <p className="text-sm text-gray-500">No news articles found.</p>
        )}
      </div>

      {/* Reddit Sentiment */}
      <div>
        <h3 className="font-medium text-gray-700">💬 Reddit</h3>
        {data.reddit && data.reddit.length > 0 ? (
          <ul className="space-y-2 mt-2">
            {data.reddit.map((post, idx) => (
              <li
                key={idx}
                className="border rounded-lg p-3 hover:bg-gray-50"
              >
                <a
                  href={post.url}
                  target="_blank"
                  rel="noreferrer"
                  className="font-medium text-blue-600 hover:underline"
                >
                  {post.title}
                </a>
                <p className="text-sm text-gray-500">
                  Sentiment:{" "}
                  <span
                    className={
                      post.sentiment.label === "positive"
                        ? "text-green-600"
                        : post.sentiment.label === "negative"
                        ? "text-red-600"
                        : "text-gray-600"
                    }
                  >
                    {post.sentiment.label} ({post.sentiment.score.toFixed(2)})
                  </span>
                </p>
              </li>
            ))}
          </ul>
        ) : (
          <p className="text-sm text-gray-500">No Reddit posts found.</p>
        )}
      </div>
    </div>
  );
}
