import { useEffect, useState } from "react";
import { showError } from "../../utils/toast";

function PaymentModal({

    open,

    booking,

    onPay,

    onClose

}) {

    const [processing, setProcessing] = useState(false);

    const [success, setSuccess] = useState(false);


    useEffect(() => {

        if (!open) {

            setProcessing(false);

            setSuccess(false);

        }

    }, [open]);


    async function handlePayment() {

        if (processing) return;

        try {

            setProcessing(true);

            await new Promise(resolve => setTimeout(resolve, 2000));

            await onPay(booking.bookingId);

            setSuccess(true);

            setTimeout(() => {

                onClose();

            }, 1000);

        }
        catch (error) {

            console.log(error);

            showError("💰 Payment failed. Please try again.");

        }
        finally {

            setProcessing(false);

        }

    }


    if (!open) {

        return null;

    }


    return (

        <div className="fixed inset-0 bg-black/40 flex justify-center items-center z-50">

            <div className="bg-white rounded-xl shadow-xl w-96 p-6">


                <h2 className="text-2xl font-bold text-center mb-6">

                    Payment

                </h2>


                {

                    !processing && !success &&

                    <>

                        <div className="space-y-3 mb-6">

                            <div>

                                <span className="font-semibold">

                                    Booking

                                </span>

                                <p>

                                    {booking.bookingNumber}

                                </p>

                            </div>

                            <div>

                                <span className="font-semibold">

                                    Vehicle

                                </span>

                                <p>

                                    {booking.vehicleNumber}

                                </p>

                            </div>

                            <div>

                                <span className="font-semibold">

                                    Slot

                                </span>

                                <p>

                                    {booking.slotNumber}

                                </p>

                            </div>

                            <div>

                                <span className="font-semibold">

                                    Amount

                                </span>

                                <p className="text-3xl font-bold text-green-600">

                                    ₹ {booking.totalAmount}

                                </p>

                            </div>

                        </div>

                        <div className="flex justify-end gap-3">

                            <button

                                onClick={onClose}

                                className="px-4 py-2 rounded bg-gray-300"

                            >

                                Cancel

                            </button>

                            <button

                                onClick={handlePayment}

                                className="px-6 py-2 rounded bg-green-600 text-white"

                            >

                                Pay

                            </button>

                        </div>

                    </>

                }


                {

                    processing &&

                    <div className="text-center py-10">

                        <div className="animate-spin rounded-full h-16 w-16 border-b-4 border-green-600 mx-auto mb-5"></div>

                        <h3 className="text-xl font-semibold">

                            Processing Payment...

                        </h3>

                        <p className="text-gray-500 mt-2">

                            Please Wait

                        </p>

                    </div>

                }


                {

                    success &&

                    <div className="text-center py-8">

                        <div className="text-6xl mb-4">

                            ✅

                        </div>

                        <h2 className="text-2xl font-bold text-green-600">

                            Payment Successful

                        </h2>

                    </div>

                }

            </div>

        </div>

    );

}

export default PaymentModal;
