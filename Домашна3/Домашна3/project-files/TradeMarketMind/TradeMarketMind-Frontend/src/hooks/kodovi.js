import { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "../context/AuthContext";

const useStockCodes = () => {
    const [codes, setCodes] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [fetchError, setFetchError] = useState(null);
    const { authToken } = useAuth();

    useEffect(() => {
        let active = true;

        const getStockCodes = async () => {
            try {
                setIsLoading(true);
                setFetchError(null);

                const response = await axios.get(
                    "http://localhost:8181/api/stock-items/codes",
                    {
                        headers: {
                            Authorization: `Bearer ${authToken}`,
                        },
                    }
                );

                if (active) {
                    setCodes(response.data || []);
                }
            } catch (err) {
                if (active) {
                    setFetchError(err.message || "Failed to fetch stock codes.");
                }
            } finally {
                if (active) {
                    setIsLoading(false);
                }
            }
        };

        getStockCodes();

        return () => {
            active = false; ;
    }, [authToken]);

    return { codes, isLoading, fetchError };
};

export default useStockCodes;
