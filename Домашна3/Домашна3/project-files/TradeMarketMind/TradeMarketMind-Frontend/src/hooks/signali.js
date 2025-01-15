import { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "../context/AuthContext";

const useTradingSignals = (ticker) => {
    const [data, setData] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [fetchError, setFetchError] = useState(null);

    const { token } = useAuth();

    const API_BASE_URL = "http://localhost:8181/api/predictions";

    useEffect(() => {
        if (!ticker) return;

        const fetchTradingSignals = async () => {
            try {
                setIsLoading(true);
                setFetchError(null);

                const response = await axios.get(`${API_BASE_URL}/signals`, {
                    params: { stockCode: ticker },
                    headers: {
                        Authorization: `Bearer ${token}`, // Додади JWT токен
                    },
                });

                if (response?.data) {
                    setData(response.data);
                } else {
                    throw new Error("No trading signals received.");
                }
            } catch (error) {
                setFetchError(error.message || "Error fetching trading signals.");
            } finally {
                setIsLoading(false);
            }
        };

        fetchTradingSignals();
    }, [ticker, token]);

    return { data, isLoading, fetchError };
};

export default useTradingSignals;
