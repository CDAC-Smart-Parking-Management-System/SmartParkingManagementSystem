function DashboardCard({

    title,

    value,

    color

}) {

    return (

        <div className="bg-white rounded-xl shadow p-6">

            <div className="flex justify-between items-center">

                <div>

                    <p className="text-gray-500 text-sm">

                        {title}

                    </p>

                    <h2 className={`text-4xl font-bold mt-2 ${color}`}>

                        {value}

                    </h2>

                </div>

            </div>

        </div>

    );

}

export default DashboardCard;