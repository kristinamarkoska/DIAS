import { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "../context/AuthContext";

const useStockSentiment = (ticker) => {
    const [data, setData] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [fetchError, setFetchError] = useState(null);

    const { token } = useAuth();

    const API_BASE_URL = "http://localhost:8181/api/predictions";

    useEffect(() => {
        if (!ticker) return;

        const fetchData = async () => {
            try {
                setIsLoading(true);
                setFetchError(null);

                const response = await axios.get(`${API_BASE_URL}/sentiments/${ticker}`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });

                if (response?.data) {
                    setData(response.data);
                } else {
                    throw new Error("No data received from the server.");
                }
            } catch (error) {
                setFetchError(error.message || "Error fetching sentiment data.");
            } finally {
                setIsLoading(false);
            }
        };

        fetchData();
    }, [ticker, token]);

    return { data, isLoading, fetchError };
};

export default useStockSentiment;
